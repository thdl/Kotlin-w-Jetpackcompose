Diagrammer:

Sekvens diagram for visning av data på hjemskjerm:
```mermaid
sequenceDiagram
    HomeScreen->>+ViewModel: Observe
    ViewModel-->>-Repositories: hentdata()
    Repositories->>+ViewModel: update values
    ViewModel->>+HomeScreen: Shows data

```
Sekvens diagram for henting av data og sjekking av paramtere:
```mermaid
sequenceDiagram
    HomeScreen->>+ViewModel: Observe
    ViewModel->>-Repositories: hentdata()
    Repositories->>+DataSources: getData()
    DataSources-->>+Repositories: return values
    Repositories-->>+Repositories: check paramteres
    Repositories-->>+ViewModel: update values and results
    ViewModel-->>+HomeScreen: Shows data
    

```
Sekvens diagram for endring av parameter:
```mermaid
sequenceDiagram
    ParameterScreen->>+ViewModel: New values
    ViewModel->>-Repositories: setParameters()
    Repositories-->>+Repositories: update parameters
    Repositories-->>+ViewModel: update values
    ViewModel-->>+ParameterScreen: Show new values
    
    
```
Sekvens diagram for endring av posisjon i kart:
```mermaid
sequenceDiagram
    HomeScreen->>+ViewModel: Observe
    MapScreen->>+ViewModel: viewModel.byttpos
    ViewModel->>-Repositories: byttpos()
    Repositories->>+Datasource: getData()
    Datasource-->>+Repositories: return new data
    Repositories-->>+Repositories: check new parameters
    Repositories-->>+ViewModel: update values
    ViewModel-->>+HomeScreen: new position alert and updated values
    
    
    
```
Use case diagram:
```mermaid
flowchart LR
   
    Bruker  --> D[Se værforhold]
    D --> V[ViewModel]
    V --> H[HentData fra API]
    Bruker --> E[Endre parameter]
    E --> V
    V --> B[ByttParameter]
    Bruker --> F[Endre posisjon]
    F --> V
    V --> SB[ByttPosisjon]
```
Klasse Diagram:
Dette klasse diagramet viser hvordan klassene samhandler med hverandre i appen.
```mermaid
    

    classDiagram
    class HomeScreen {
        +HomeScreen(navController: NavHostController, viewModel: HomeScreenViewModel)
    }
    
    class ParameterScreen {
        +ParameterScreen(viewModel: HomeScreenViewModel)
    }
    
    class MapScreen {
        +MapScreen(navController: NavHostController, viewModel: HomeScreenViewModel, context: Context)
    }
    
    class HomeScreenViewModel {
        -_homeScreenUiState: MutableStateFlow<HomeUiState>
        -vindRepository: VindRepository
        -initializeCalled: Boolean
        -nedbørRepository: nedbørRepositoryImplementation
        -long: String
        -lat: String
        -_showDialog: MutableStateFlow<Boolean>
        +hentdata(long: String, lat: String)
        +byttpos(longbyt: String, latbyt: String)
        +updateMarkerPos(latlng: LatLng)
        +dismissDialog()
        +updateCameraPos(cameraPosistion: CameraPosition)
        -loadInfo()
        +setParameter(regn: String, sky: String, tåke: String, gust: String, dew: String, hum: String, sheer: String, luft: String)
        +setDefault()
    }
    
        class HomeUiState {
        -nedbor: List<Int>
        -nedborGood: Boolean?
        -skyGood: Boolean?
        -tåkeGood: Boolean?
        -dewGood: Boolean?
        -humGood: Boolean?
        -vindOver10: VindOver10mDataklasse?
        -vindUnder10: MutableList<MutableList<String>>?
        -nedbør: Double?
        -siktSkydekke: Double?
        -siktTåke: Double?
        -vindUnder10Good: Boolean?
        -totalvurdering: Boolean?
        -long: String
        -lat: String
        -markerLatLng: LatLng?
        -cameraPosistion: CameraPosition?
        -dewPoint: Double?
        -relativeHumidity: Double?
        -maxRegn: Double
        -maxTåke: Double
        -maxSkydekke: Double
        -maxGust: Double
        -maxDewPoint: Double
        -maxRelativHumidity: Double
        -maxSheerWind: Double
        -maxVindILufta: Double
    }
    

    
    class VindRepository {
        -maxGust: Double
        -maxSheerWind: Double
        -maxVindILufta: Double
        +getDataOver10m(lat: String, lon: String, maksVind: String, maksShear: String): VindOver10mDataklasse
        +vindOver10mgood(data: VindOver10mDataklasse): Boolean
        +getDataUnder10m(lat: String, lon: String): MutableList<MutableList<String>>
        +vindunder10good(long: String, lat: String): Boolean
        +setMaxGust(ny: Double)
        +getMaxGust(): Double
        +setMaxSheerWind(ny: Double)
        +getMaxSheerWind(): Double
        +setMaxVindILufta(ny: Double)
        +getMaxVindILufta(): Double
        +getWindDirection(angle: Double): String
        +hent_vind_pil(vinkel: Double): String
    }
    
    class WindDataOver10mDataSource {
        +get_data_from_backendServer(lat: String, lon: String, maksVind: String, maksShear: String): VindOver10mDataklasse
    }
    
    class WindDataUnder10mDataSource {
        +get_wind_speed_of_gust(lat: String, lon: String): WindUnder10mDataClass
    }
    
    class nedbørRepository {
        +updateNedbør(long: String, lat: String, alt: String)
        +getNedbør(long: String, lat: String, alt: String): Double?
        +getSiktSkydekke(long: String, lat: String, alt: String): Double?
        +getSiktTåke(long: String, lat: String, alt: String): Double?
        +regnGood(long: String, lat: String, alt: String): Boolean?
        +tåkeGood(long: String, lat: String, alt: String): Boolean?
        +skydekkeGood(long: String, lat: String, alt: String): Boolean?
        +dewPointGood(long: String, lat: String, alt: String): Boolean?
        +relativHumGood(long: String, lat: String, alt: String): Boolean?
        +getDewPoint(long: String, lat: String, alt: String): Double?
        +getRelativeHumidity(long: String, lat: String, alt: String): Double?
        +setMaxRegn(regn: Double)
        +setMaxSiktTåke(tåke: Double)
        +setMaxSiktSkydekke(sky: Double)
        +setMaxDewPoint(dew: Double)
        +setMaxRelativHumidity(humidity: Double)
        +getMaxRegn(): Double
        +getMaxSiktSkydekke(): Double
        +getMaxSiktTåke(): Double
        +getMaxDewPoint(): Double
        +getMaxRelativHumidity(): Double
    }
    
    class nedbørRepositoryImplementation {
        -nedbørDataSource: nedbørDataSource
        -maxRegn: Double
        -maxSiktTåke: Double
        -maxSiktSkydekke: Double
        -maxDewPoint: Double
        -maxRelativHumidity: Double
        -nedBørResponse: NedBorResponse
        +updateNedbør(long: String, lat: String, alt: String)
        +getNedbør(long: String, lat: String, alt: String): Double?
        +getSiktSkydekke(long: String, lat: String, alt: String): Double?
        +getSiktTåke(long: String, lat: String, alt: String): Double?
        +regnGood(long: String, lat: String, alt: String): Boolean
        +tåkeGood(long: String, lat: String, alt: String): Boolean?
        +skydekkeGood(long: String, lat: String, alt: String): Boolean?
        +dewPointGood(long: String, lat: String, alt: String): Boolean?
        +relativHumGood(long: String, lat: String, alt: String): Boolean?
        +getDewPoint(long: String, lat: String, alt: String): Double?
        +getRelativeHumidity(long: String, lat: String, alt: String): Double?
        +setMaxRegn(regn: Double)
        +setMaxSiktTåke(tåke: Double)
        +setMaxSiktSkydekke(sky: Double)
        +setMaxDewPoint(dew: Double)
        +setMaxRelativHumidity(humidity: Double)
        +getMaxRegn(): Double
        +getMaxSiktSkydekke(): Double
        +getMaxSiktTåke(): Double
        +getMaxDewPoint(): Double
        +getMaxRelativHumidity(): Double
    }
    
    class LatLng {
        # LatLng class from Google Maps SDK
    }
    
    class CameraPosition {
        # CameraPosition class from Google Maps SDK
    }
    
    class VindOver10mDataklasse {
        # VindOver10mDataklasse class
    }
    
    class WindUnder10mDataClass {
        -geometry: Geometry
        -properties: Properties
        -type: String
    }
    
 
    
    
    class NedBorResponse {
        # NedBorResponse class
    }
    class BaseLayout {
        +BaseLayout(regn: Double?, sky: Double?, tåke: Double?, dew: Double?, hum: Double?, regnG: Boolean?, skyG: Boolean?, tåkeG: Boolean?, dewG: Boolean?, humG: Boolean?)
    }
    class WindDataOver10mDataSource {
        +get_data_from_backendServer(lat: String, lon: String, maksVind: String, maksShear: String) VindOver10mDataklasse
    }

    class HttpClient {
        <<Ktor>>
    }

    class VindOver10mDataklasse {
        -vindData: Map<String, DataTrykkFlate>
    }

    class DataTrykkFlate {
        -meterOverHavet: Double
        -retning: Double
        -shearInnenforMax: Boolean
        -shearvind: Double
        -styrke: Double
        -temp: Double
        -uKomponent: Double
        -vKomponent: Double
        -vindInnenforMax: Boolean
    }
    class nedbørDataSource {
        -client: HttpClient
        +getNedbør(lat: String, lon: String, altitude: String) NedBorResponse
        +getNedbør1() NedBorResponse?
    }


    class NedBorResponse {
        -geometry: Geometry
        -properties: Properties
        -type: String
    }
    
    
    
    HomeScreen ..> HomeScreenViewModel
    HomeScreen ..> BaseLayout
    ParameterScreen ..> HomeScreenViewModel
    MapScreen ..> HomeScreenViewModel
    HomeScreenViewModel ..> HomeUiState
    HomeScreenViewModel ..> VindRepository
    HomeScreenViewModel ..> nedbørRepositoryImplementation
    HomeScreenViewModel ..> LatLng
    HomeScreenViewModel ..> CameraPosition
    HomeUiState ..> VindOver10mDataklasse
    HomeUiState ..> WindUnder10mDataClass
    nedbørRepositoryImplementation ..> nedbørRepository
    nedbørRepository ..> nedbørDataSource
    VindRepository ..> WindDataOver10mDataSource
    VindRepository ..> WindDataUnder10mDataSource
    WindDataOver10mDataSource ..> HttpClient
    WindDataOver10mDataSource --> VindOver10mDataklasse
    VindOver10mDataklasse *-- DataTrykkFlate
    WindDataUnder10mDataSource ..> WindUnder10mDataClass
    nedbørDataSource --> NedBorResponse

   
   
```
Flowchartene viser hvordan vi får frem data fra APIene til repositories og videre til en viewmodel der skjermene tar dataen og viser det.
Flowchart V1:
```mermaid
flowchart TD
    A[HomeScreen] -->B[ViewModel]
    A1[ParameterScreen] -->B
    A2[MapScreen] -->B
    B --> C[Nedbør Repository]
    B --> D[Sikt Repository]
    B --> E[Vind Repository]
    C --> F[Nedbør Datasource]
    D --> G[Sikt Datasource]
    E --> H[Vind Datasource]
    F--> I((LocationAPI))
    G--> I
    H--> J((Isobarcgrib API))

```
Flowchart V2:
For den oppdaterte flowcharten valgte vi å kombinere nedbør repository og sikt repository.
```mermaid
  flowchart TD
    A[HomeScreen] -->B[ViewModel]
    A1[ParameterScreen] -->B
    A2[MapScreen] -->B
    B --> C[Nedbør Repository]
    B --> E[Vind Repository]
    C --> F[Nedbør Datasource]
    C --> G[Sikt Datasource]
    E --> H[Vind Datasource]
    F--> I((LocationAPI))
    G--> I
    H--> J((Isobarcgrib API))

```


