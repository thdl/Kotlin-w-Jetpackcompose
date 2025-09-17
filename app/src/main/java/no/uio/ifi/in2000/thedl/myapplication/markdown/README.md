Prosjekt oversikt:
- Dette prosjektet er en app som skal vurdere om forholdene er gunstige for en rakket-oppskytning. Appen tar hensyn til ulike paramtere
for været og vurderer om de ligger innenfor grensene for en oppskytning.

- Hovedfunksjonaliteten i appen inkluderer:
  - Vise en oversikt over gjeldene værforhold
  - Sjekke om foholdene er innenfor parametere
  - Justere og tilpasse parameterne for oppsktyning
  - Presentere et kart der bruker kan velge egen posisjon eller en annen poisjon
  - Kartet presenterer også et utvalg av områder der det potensielt kan være ok for oppskytning og info om området og kontakt info

- Veiledning:
  - Valgt API level er 33
  - Det er en navigasjonsbar, den har tre knapper som viser til tre skjermer
  - Når appen startes skal den åpne på homescreen, denne skjermen viser forholdene for en oppskytning og om det er er innenfor.
    - Dersom det er ok vil det vises en en grønn rakket, men dersom det ikke er innenfor vises en rød sirkel på 4 kolonne av tabellen.
    - 1 kolonne viser et symbol
    - 2 kolonne viser navnet på symbolet
    - 3 kolonne viser dataen om symoblet
    - 4 kolonne viser om forholdet er ok for det symbolet
    - Oppskyttning er ok dersom det er en grønn rakket for hver rad.
  - Du kan navigere til parameterskjern på navigasjonsbaren er en checkmark og endre på parametere
    - På denne skjermen er det 8 parametere å endre på
    - Fra start er den satt til standard parametere vi har fått fra Portal Space
    - På bunnen er det to knapper, den ene rester parametere til default verdiene fra Portal Space og den andre lagrer de nye parameterne og henter data til homescreen
  - Siste knapp er til kart skjermen
    - Når man først kommer inn på denne skjermen skal den spørre om tilattelse til å få posisjonen til enheten,
      - Dersom man får tilattelse kommer en blå marker som viser din posisjon, denne kan trykkes på og det kommer en tag som viser at dette er din posisjon
      - Dersom du ikke får tilattelse kommer posisjonen til å defaulte til ole johan dahls hus, når appen starter så er poisjon satt til lat "59",long: "10" Dette er da Oslo 
      - Uansett om det er gitt tilattelse om posisjon eller ikke kan bruker endre på posisjonen til hvilke som helst posisjon i Norge ved Å long press på kartet, da vil en grønn marker poppe opp der bruker trykket.
        - Denne markeren kan trykkes på som vil vise en tag, med cirka latitude og longtitude koordinater
        - Appen kommer kun til å bruke devicen sin posisjon til å hente data når det kommer opp popup om å tilatte å bruke gps, ellers viser den bare den blå markeren
      - Hver gang brukeren bytter posisjon kommer en alergdialog på homescreen som varsler om den nye posisjonen
      - På kartet er det markert områder der det kan være lovlig å skytte rakketer, disse områdene er hentet fra Avinor
        - Områdene er markert rødt som en polygon og kan trykkes på får å få opp en snackbox som viser diverse info om området

Backend server:
  - backend-serveren implementert med Flask, som kjører på PythonAnywhere. Serveren tilbyr et API-endepunkt for å sjekke vindforhold ved hjelp av data fra GRIB2-filer.
    - Avhengigheter:
      - Flask, pygrib, numpy, requests
    - Endepunkt:
      - http://rondestvedt.pythonanywhere.com/check-wind-conditions
      - Metode: "GET"
      - Query Parameters:
        - lat (float): Breddegrad for det spesifikke punktet (default: 59.91)
        - lon (float): Lengdegrad for det spesifikke punktet (default: 10.75)
        - maks_vind (float): Maksimum tillatt vindstyrke (default: 17.2)
        - maks_shear (float): Maksimum tillatt shearvind (default: 24.5)
      - Eksempel: curl http://rondestvedt.pythonanywhere.com/check-wind-conditions?lat=59.91&lon=10.75&maks_vind=17.2&maks_shear=24.5
        - Respons: {
                    "850": {
                      "u-komponent": 3.0,
                      "v-komponent": 2.0,
                      "temp": -5.0,
                      "styrke": 3.6,
                      "retning": 56.3,
                      "shearvind": 0.5,
                      "meterOverHavet": 1450.0,
                      "vindInnenforMax": true,
                      "shearInnenforMax": true
                      },
                      ...
                    }
    


Bibloteker og dependecies:
  - Android Studio
  - Android Patform SDK API level 31 eller høyere
  - Android Build Tools
  - Android Emulator (For testing)
  - Google Play Servives
  - Google Maps API med API key

  - Android Core:
    - androidx.core:core-ktx (Kotlin extensions for Android framework classes)
    - androidx.lifecycle:lifecycle-runtime-ktx (Lifecycle components with Kotlin extensions)
    - androidx.activity:activity-compose (Integration between Android Activity and Jetpack Compose)
    
  - Jetpack Compose:
    - androidx.compose.ui:ui (Core Compose UI components)
    - androidx.compose.ui:ui-graphics (Graphics utilities for Compose)
    - androidx.compose.ui:ui-tooling-preview (Tooling and preview utilities)
    - androidx.compose.material3:material3 (Material Design 3 components)
    - androidx.lifecycle:lifecycle-viewmodel-compose (ViewModel integration)
    - androidx.navigation:navigation-compose (Navigation components)
    - androidx.lifecycle:lifecycle-runtime-compose (Lifecycle integration)
    
  - Testing:
    - junit:junit (JUnit testing framework)
    - androidx.test.ext:junit (Android testing extensions for JUnit)
    - androidx.test.espresso:espresso-core (Espresso UI testing framework)
    - androidx.compose.ui:ui-test-junit4 (Compose UI testing utilities)
    - org.jetbrains.kotlinx:kotlinx-coroutines-test (Testing utilities for coroutines)
    - io.mockk:mockk (Mocking library for Kotlin)

  - Compose Debugger:
    - androidx.compose.ui:ui-tooling (Tooling utilities for Compose)
    - androidx.compose.ui:ui-test-manifest (Testing and manifest utilities for Compose)
  
  - Ktor:
    - io.ktor:ktor-serialization-gson (JSON serialization/deserialization with Gson)
    - io.ktor:ktor-client-core (Ktor HTTP client core)
    - io.ktor:ktor-client-android (Ktor HTTP client for Android)
    - io.ktor:ktor-client-content-negotiation (Content negotiation utilities)
    - io.ktor:ktor-client-cio (Coroutine I/O support for Ktor HTTP client)
  
  - Location & Maps:
    - com.google.android.gms:play-services-location (Google Play Services Location APIs)
    - com.google.maps.android:maps-compose (Google Maps integration for Compose)
    - com.google.android.libraries.places:places (Google Places APIs)
    - pub.devrel:easypermissions (Runtime permissions handling library)
    
  - Splash Screen:
    - androidx.core:core-splashscreen (APIs and utilities for implementing a splash screen)
  


