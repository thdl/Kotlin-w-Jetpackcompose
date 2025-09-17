from flask import Flask, request, jsonify
import pygrib
import numpy as np
import os

app = Flask(__name__)


def finn_nearmeste(lat_lon_array, punkt):
    avstander = np.sqrt((lat_lon_array[:,0] - punkt[0])**2 + (lat_lon_array[:,1] - punkt[1])**2)
    return np.argmin(avstander)


def hent_data_for_omraadet(fil, lat, lon):
    
    data = {}

    with pygrib.open(fil) as grbs:
        grb = grbs.select()[0]
        grbs_lats, grbs_lons = grb.latlons()

        lat_lon_array = np.dstack([grbs_lats.ravel(), grbs_lons.ravel()])[0]
        idx = finn_nearmeste(lat_lon_array, (lat, lon))

        for grb in grbs:
            hPa = grb.level
            if hPa not in data:
                data[hPa] = {"u-komponent":None,"v-komponent":None,"temp":None}
            if grb.name == "U component of wind":
                data[hPa]["u-komponent"] = grb.values.ravel()[idx]
            elif grb.name == 'V component of wind':
                data[hPa]["v-komponent"] = grb.values.ravel()[idx]
            elif grb.name == 'Temperature':
                data[hPa]["temp"] = grb.values.ravel()[idx]
    print(data)
    return data


def beregn_gjennomsnitt(data1, data2):
    gjennomsnitt_data = {}

    for hPa in data1:
        if hPa in data2:
            gjennomsnitt_data[hPa] = {
                "u-komponent": (data1[hPa]["u-komponent"] + data2[hPa]["u-komponent"]) / 2,
                "v-komponent": (data1[hPa]["v-komponent"] + data2[hPa]["v-komponent"]) / 2,
                "temp": (data1[hPa]["temp"] + data2[hPa]["temp"]) / 2,
            }
    return gjennomsnitt_data

def legg_til_hastighet_og_retning(data):
    oppdatert_data = {}

    for hPa, komponenter in data.items():
        u = komponenter["u-komponent"]
        v = komponenter["v-komponent"]

        styrke = np.sqrt(u**2 + v**2)
        retning = np.arctan2(-u, -v) * (180 / np.pi) % 360

        oppdatert_data[hPa] = {
            "u-komponent": u,
            "v-komponent": v,
            "temp": komponenter["temp"],
            "styrke": styrke,
            "retning": retning
        }
    return oppdatert_data

def legg_til_vindshear(data):
    oppdatert_data = {}

    trykknivaaer = sorted(data.keys(), reverse=True)

    for i in range(len(trykknivaaer) - 1):
        oppdatert_data[trykknivaaer[i]] = data[trykknivaaer[i]].copy()

        lag1 = data[trykknivaaer[i]]
        lag2 = data[trykknivaaer[i + 1]]

        delta_styrke = lag2['styrke'] - lag1['styrke']
        delta_retning = (lag2['retning'] - lag1['retning']) % 360
        if delta_retning > 180:
            delta_retning -= 360
        
        oppdatert_data[trykknivaaer[i]]["shearvind"] = abs(delta_styrke)

    #oppdatert_data[trykknivaaer[-1]] = oppdatert_data[trykknivaaer[-1]].copy()
    #oppdatert_data[trykknivaaer[-1]]['shearvind'] = None

    return oppdatert_data

#kan legges til seinere evt
def hent_lufttrykk_havnivaa():
    pass

#trengs celsius?
def legg_til_celsius():
    pass


def legg_til_m_over_havnivaa(data):
    oppdatert_data = {}

    P0 = 1013.25 #standard luftrykk ved havnivå i hPa (bra nok?)

    for hPa, attributt in data.items():
        P = float(hPa)
        T = attributt["temp"]

        h = (P0 - P) / (0.00012 * T + 0.12)

        ny_atributt = attributt.copy()
        ny_atributt["meterOverHavet"] = h

        oppdatert_data[hPa] = ny_atributt

    return oppdatert_data
    
def sjekk_om_innenfor_maxverdier(data, maks_vind, maks_shear):
    oppdatert_data = {}

    for hPa, attributt in data.items():

        oppdatert_data[hPa] = attributt.copy()

        oppdatert_data[hPa]["vindInnenforMax"] = attributt["styrke"] <= maks_vind

        shearvind = attributt.get("shearvind", 0)
        oppdatert_data[hPa]["shearInnenforMax"] = shearvind <= maks_shear

    return oppdatert_data

def konverter_numpy_bool_til_python(verdi):
    if isinstance(verdi, dict):
        return {k: konverter_numpy_bool_til_python(v) for k, v in verdi.items()}
    elif isinstance(verdi, list):
        return [konverter_numpy_bool_til_python(v) for v in verdi]
    elif isinstance(verdi, np.bool_):
        return bool(verdi)
    else:
        return verdi

def runde_av_verdier(data):
    for key in data.keys():
        data[key]["meterOverHavet"] = round(data[key]["meterOverHavet"], 2)
        data[key]["retning"] = round(data[key]["retning"], 2)
        data[key]["shearvind"] = round(data[key]["shearvind"], 2)
        data[key]["styrke"] = round(data[key]["styrke"], 2)
        data[key]["temp"] = round(data[key]["temp"], 2)
        data[key]["u-komponent"] = round(data[key]["u-komponent"], 2)
        data[key]["v-komponent"] = round(data[key]["v-komponent"], 2)
    return data



@app.route("/check-wind-conditions", methods=["GET"])
def lever_gjennomsnitt_neste_3_timer():
    lat = float(request.args.get("lat", default=59.91, type=float))
    lon = float(request.args.get("lon", default=10.75, type=float))

    maks_vind = request.args.get("maks_vind", default=17.2, type=float)
    maks_shear = request.args.get("maks_shear", default=24.5,type=float)





    BASE_DIR = os.path.dirname(os.path.abspath(__file__))


    fil1 = os.path.join(BASE_DIR,"files", "fil1")
    fil2 = os.path.join(BASE_DIR, "files", "fil2")

    data_fra_fil1 = None
    data_fra_fil2 = None

    if os.path.exists(fil1):
        data_fra_fil1 = hent_data_for_omraadet(fil1, lat, lon)
    if os.path.exists(fil2):
        data_fra_fil2 = hent_data_for_omraadet(fil2, lat, lon)

    datasets = [dataset for dataset in [data_fra_fil1, data_fra_fil2] if dataset is not None]
    if len(datasets) >= 1:
        #regner gjennomsnitt for neste 3 timer
        gjennomsnitt_neste_3_timer = datasets[0] if len(datasets) == 1 else beregn_gjennomsnitt(datasets[0], datasets[1])

        #regner og legger til retning og styrke
        gjennomsnitt_neste_3_timer = legg_til_hastighet_og_retning(gjennomsnitt_neste_3_timer)

        #regner og legger til shearvind
        gjennomsnitt_neste_3_timer = legg_til_vindshear(gjennomsnitt_neste_3_timer)

        #regner og legger til m over havet
        gjennomsnitt_neste_3_timer = legg_til_m_over_havnivaa(gjennomsnitt_neste_3_timer)

        #regner og legger til sjekk for max verdier
        gjennomsnitt_neste_3_timer = sjekk_om_innenfor_maxverdier(gjennomsnitt_neste_3_timer, maks_vind, maks_shear)

        #konverterer numpy bool
        gjennomsnitt_neste_3_timer = konverter_numpy_bool_til_python(gjennomsnitt_neste_3_timer)

        #runder av verdier
        gjennomsnitt_neste_3_timer = runde_av_verdier(gjennomsnitt_neste_3_timer)



        return jsonify(gjennomsnitt_neste_3_timer)
    else:
        return jsonify({"error": "Ingen filer funnet for beregning"}), 404
    



if __name__ == "__main__":
    app.run(debug=True)


