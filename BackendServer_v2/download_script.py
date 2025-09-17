import requests
from datetime import datetime,timedelta,timezone
import os

header = {"User-Agent": "joharond@uio.no"}



def hent_tilgjengelige_gribfiler():
    api_URL = "https://api.met.no/weatherapi/isobaricgrib/1.0/available.json?type=grib2&area=southern_norway"
    

    svar = requests.get(api_URL, headers=header)

    if svar.status_code == 200:
        return svar.json()
    else:
        print(f"Feil ved henting av tilgjengelige filer: {svar.status_code}")
        return None


def slett_gamle_filer():
    for fil_navn in ["fil1", "fil2"]:
        try:
            os.remove(fil_navn)
            print(f"Slettet gammel fil: {fil_navn}")
        except FileNotFoundError:
            print(f"Fant ikke filen {fil_navn}, fortsetter...")


def last_ned_relevante_filer(tilgjengelige_filer):
    naa = datetime.now(timezone.utc)
    relevante_filer = []

    for fil in tilgjengelige_filer:
        fil_tid = datetime.fromisoformat(fil["params"]["time"].replace("Z","+00:00"))
        if naa <= fil_tid <= naa + timedelta(hours=3):
            relevante_filer.append(fil)


    if len(relevante_filer) > 2:
        print(f"Relevante_filer inneholder for mange filer.. antall:{len(relevante_filer)}")
        return None

    nedlastingsmappe = "files"
    if not os.path.exists(nedlastingsmappe):
        os.makedirs(nedlastingsmappe)

    for indeks, fil in enumerate(relevante_filer):
        uri = fil["uri"]
        fil_navn = os.path.join(nedlastingsmappe, f"fil{indeks+1}")
        svar = requests.get(uri, headers=header)
        if svar.status_code == 200:
            with open(fil_navn, "wb") as f:
                f.write(svar.content)
            print(f"Fil lastet ned: {fil_navn}")
        else:
            print(f"Feil med nedlasting av: {uri}: {svar.status_code}")
            return None
        
if __name__ == "__main__":
    tilgjengelige_filer = hent_tilgjengelige_gribfiler()
    if tilgjengelige_filer:
        slett_gamle_filer()
        last_ned_relevante_filer(tilgjengelige_filer)

            

