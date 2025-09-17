from flask import Flask, request, jsonify
import requests
import subprocess
import os
import logging
import json
import numpy as np
from datetime import datetime, timedelta, timezone
from apscheduler.schedulers.background import BackgroundScheduler




app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

def get_relevant_grib_files(next_hours=2):
    API_URL = "https://api.met.no/weatherapi/isobaricgrib/1.0/available.json?type=grib2&area=southern_norway"
    headers = {"User-Agent": "joharond@uio.no"}
    try:
        response = requests.get(API_URL,headers=headers)
        response.raise_for_status()
        datasets = response.json()
        

        now = datetime.now(timezone.utc)
        end_window = now + timedelta(hours=next_hours)

        relevant_uris = []

        for dataset in datasets:
            forecast_time = datetime.strptime(dataset['params']['time'],"%Y-%m-%dT%H:%M:%SZ").replace(tzinfo=timezone.utc)

            if now <= forecast_time <= end_window:
                relevant_uris.append(dataset['uri'])
            #her burde det være en sjekk i tilfellet api ikke har filer for gitt tidspunkt
        else: relevant_uris.append(datasets[0]['uri'])
            
        return relevant_uris
    except requests.RequestException as e:
        print(f"feil med api kall /available {e}")
        return []

get_relevant_grib_files()
scheduler = BackgroundScheduler()

def scheduled_job():
    relevant_files = get_relevant_grib_files()
    print(relevant_files)
    download_and_convert_isobaricgrib(relevant_files[0])
    # for uri in relevant_files:
    #     download_and_convert_isobaricgrib(uri)

scheduler.add_job(scheduled_job, "interval", hours=1, next_run_time=datetime.now())

scheduler.start()





def download_and_convert_isobaricgrib(uri):
    headers = {"User-Agent": "joharond@uio.no"}
    
    
    grib_file_url = uri

    output_json_file = "output.json"

    current_directory = os.path.dirname(os.path.abspath(__file__))
    grib2json_cmd = os.path.join(current_directory, "grib2json-0.8.0-SNAPSHOT", "bin", "grib2json.cmd")

    

    try:
        response = requests.get(grib_file_url, headers=headers)
        if response.status_code == 200:
            local_grib_file_path = 'temp.grib2'
            with open(local_grib_file_path, 'wb') as file:
                file.write(response.content)

            subprocess.run([grib2json_cmd, "--data", "--names","--fp", "wind", "--output", output_json_file, local_grib_file_path], check=True)

            with open(output_json_file, 'r') as file:
                json_data = file.read()

           
            return {"success": True, "message": "Operasjon fullført"}
    except Exception as e:
        return {"success": False, "error": str(e)}



@app.route('/check-wind-conditions', methods=['GET'])
def api_check_wind_conditions():


    conditions_acceptable = is_wind_conditions_acceptable()
    #burde returnere tid på filen som er sjekket

    return jsonify({
        "is_acceptable": conditions_acceptable,
        "message": "Wind conditions are acceptable." if conditions_acceptable else "Wind conditions are not acceptable."
    })


def is_wind_conditions_acceptable():
    winds, shears = get_wind_data_for_location()

    all_speeds_within_limit = all(0 < speed <= 17.2 for speed in winds)
    all_shears_within_limit = all(0 < shear <= 24.5 for shear in shears)

    print(winds)
    print(shears)

    return all_speeds_within_limit and all_shears_within_limit


def get_wind_data_for_location(lat = 59.91, lon = 10.75):
    nx=160
    ny=90
    lo1=358.55
    la1=55.35
    dx=0.1
    dy=0.1


    speeds, shears = read_and_prosess_jsondata()

    x_index = int((lon - lo1) / dx) % nx
    y_index = int((lat - la1) / dy) % ny

    location_wind_speeds = [ws[y_index, x_index] for ws in speeds]
    location_wind_shears = [ws[y_index,x_index] for ws in shears]

    return location_wind_speeds,location_wind_shears


def read_and_prosess_jsondata():
    wind_speeds = []
    wind_shears = []

    with open("output.json", 'r') as f:
        json_data = json.load(f)

    u_components = [objekt for objekt in json_data if objekt['header']['parameterNumberName'] == 'U-component_of_wind']
    v_components = [objekt for objekt in json_data if objekt['header']['parameterNumberName'] == 'V-component_of_wind']

  

    for i in range(len(u_components)):  
        u_data_current = np.array(u_components[i]['data']).reshape(90, 160)
        v_data_current = np.array(v_components[i]['data']).reshape(90, 160)

        wind_speed_current = np.sqrt(u_data_current**2 + v_data_current**2)
        wind_speeds.append(wind_speed_current)

        
        if i < len(u_components) - 1:
            u_data_next = np.array(u_components[i + 1]['data']).reshape(90, 160)
            v_data_next = np.array(v_components[i + 1]['data']).reshape(90, 160)

            du = u_data_next - u_data_current
            dv = v_data_next - v_data_current

            wind_shear = np.sqrt(du**2 + dv**2)
            wind_shears.append(wind_shear)

   

    return wind_speeds, wind_shears

    

if __name__ == '__main__':
    app.run(debug=True)



# import math

# def calculate_wind_direction(u, v):
#     direction = 270 - math.atan2(v, u) * (180 / math.pi)
    
#     direction = direction % 360
#     return direction


# u_component = -10  #Vind fra vest
# v_component = 10   #Vind fra sør

# wind_direction = calculate_wind_direction(u_component, v_component)