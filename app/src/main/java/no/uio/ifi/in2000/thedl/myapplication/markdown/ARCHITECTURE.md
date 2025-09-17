Liten forklaring til arkitekturen:

Arkitekturen til appen vår er preget av at vi henter fra to forskjellige API-er, LocationForecast2 og IsobaricGrib.
Vi tar i bruk to repositories en for hver sin API. Videre har vi bare en viewmodel som vi bruker på alle skjermene.
Appen har tre skjermer, en til å se dataen og status, en til å sette parametere og som bestemmer hvordan været skal være under oppskytning og en til å bestemme posisjon gjennom et kart.
Man kan navigere gjennom de ulike skjermene med en navigasjonsbar på bunden av skjermen. Den brukes også til å sende informasjon mellom skjermene.
Det er ulike måter å gjøre dette på, men vi bestemte på at vi lager en felles viewModel når appen kjøres og delegerer denne rundt til skjermen som åpnes gjennom navigasjonsbaren.
Vi fant denne måten til å være den letteste for oss, men også sikrest for appen.

Forklaring av MVVM modellen i appen:

Model

--LocationForecast2--
Appen henter inn data fra met sin LocationForecast2 API gjennom UIO-proxyen, dette gjøres i nedBorDataSource klassen og returnerer en data klasse som heter NedBorResponse.
Denne inneholder andre data klasser slik at vi kan bevare informasjonen som vi får fra API-et. Vi bruker Ktor til å få tak i et json objekt som passer inn i data klassene våre.
nedBørRepository oppretter en slik datasource og lagrer en variabel for NedBorResponse data klassen slik at den blir lagra her.
Selvet repositoriet har mange funksjoner men den aller viktigste er updateNedbør som kaller på datasource sin funksjon for å gjøre et api kall som vi lagrer i NedBorResponse variabelen.
Vi fant at denne måten å gjøre det på vil være best for å minske antallet api kall som gjøres. Update funksjonen kalles i praksis når vi endrer posisjon fordi apiet viser data for et bestemt området.
De andre funksjonene til nedBørRepositoriet henter og manipulerer verdier slik at vi kan vise de på skjermen.

--IsobaricGrib--

vindDataOver10mDataSource henter data fra API-et i GRIB format fra et python program og parser det over til en json.
Dataen hentes i repositorien ved å kalle på data sourcen sin get funksjon.

View model

Appen vår bruker bare en viewModel, det lages en viewModel når man åpner appen og navigasjonsbaren delegerer denne rundt når man bytter skjerm.
Denne bevarer på mye av dataen i en Ui-state.

View

Vi har tre skjermer en HomeScreen som viser alle dataene på skjermen og lar brukeren se om det er klart til å skyte opp en rakett.
Den andre skjermen er en parameter skjerm som lar brukeren selv sette ulike parametere etter rakettens nød. Den siste er en skjerm for kartet som lar brukeren velge en posisjon.


---

Forklaring av prinsipper vi tar i bruk:
Vi sikrer lav kobling ved at vi setter av en fil til hver klasse og bruker disse sine funksjoner til å sikre at koden vår blir både finere og mer lesbar, samt lettere å debugge ettersom vi finner feil.
Ved å gjøre dette har vi mange ulike funksjonelle biter som vi setter sammen ved hjelp av MVVM modellen. I vårt tilfelle blir modellen repositoriene og datasourcene som handler en stor del av backend-en vår. 
Vi har en viewModel som separerer skjermene vi ser og modellen og håndterer logikken, og vi har skjermer som viser et UI. Appen har høy kohesjon fordi vi bruker MVVM modellen til å separere ansvar og fokuset til de ulike kodefilene.
Vi har også UDF slik at når UI-et gjør noe som f.eks. save changes på parameterskjermen så oppdateres staten i viewmodelen og sendes tilbake oppdatert til skjermen.
Forklaring av API nivå og vedlikehold av appen.

Appen bruker mange av Android sine biblioteker samt Ktor, google maps, mockk og jetpack compose.
Den eneste vedlikeholden som kan hende at appen trenger er at hvis API-et forandrer seg så må også data klassene og dataet bli lagret annerledes.
Vi bruker API nivå 33 fordi det lar flest mulig bruke appen og ingen av funksjonene vi tar i bruk er utdaterte i denne.
Det er noen funksjoner som blir depracated etter API nivå 34 og for å være helt trygge kjører vi på 33.
