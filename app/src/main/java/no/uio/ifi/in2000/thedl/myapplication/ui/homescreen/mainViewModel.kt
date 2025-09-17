package no.uio.ifi.in2000.thedl.myapplication.ui.homescreen


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.thedl.myapplication.data.nedbor.nedborRepository
import no.uio.ifi.in2000.thedl.myapplication.data.nedbor.nedborRepositoryImplementation
import no.uio.ifi.in2000.thedl.myapplication.data.vind.VindRepository
import no.uio.ifi.in2000.thedl.myapplication.model.VindOver10mDataklasse


data class HomeUiState(
    // Lagre variabler her fra nor viewmodel oppdateres
    var nedborGood: Boolean? = null,
    var skyGood: Boolean? = null,
    var tokeGood: Boolean? = null,
    var dewGood: Boolean? = null,
    var humGood: Boolean? = null,
    var vindOver10: VindOver10mDataklasse? = null,
    var vindOver10Good: Boolean? = null,
    var vindUnder10: MutableList<MutableList<String>>? = null,
    var nedbor: MutableList<MutableList<String>>? = null,
    var siktSkydekke: MutableList<MutableList<String>>? = null,
    var siktToke: MutableList<MutableList<String>>? = null,
    var vindUnder10Good: Boolean? = null,
    var long: String = "59.911491",
    var lat: String = "10.757933",
    var markerLatLng: LatLng? = null,
    var cameraPosistion: CameraPosition? = null,
    var dewPoint: MutableList<MutableList<String>>? = null,
    var relativeHumidity: MutableList<MutableList<String>>? = null,
    var maxRegn: Double = 0.0,
    var maxToke: Double = 0.0,
    var maxSkydekke: Double = 0.0,
    var maxGust: Double = 0.0,
    var maxDewPoint: Double = 0.0,
    var maxRelativHumidity: Double = 0.0,
    var maxSheerWind: Double = 0.0,
    var maxVindILufta: Double = 0.0,
    var adrok: String? = "false"
)

class HomeScreenViewModel : ViewModel() {

    // Hent repositories her og andre variabler som skal brukes i homescreen
    private val _homeScreenUiState = MutableStateFlow(HomeUiState())
    val homeScreenUiState: StateFlow<HomeUiState> = _homeScreenUiState.asStateFlow()
    private val vindRepository: VindRepository = VindRepository()
    private val nedborRepository: nedborRepository = nedborRepositoryImplementation()
    var long: String = "59.911491"
    var lat: String = "10.757933"
    private val _showDialog = MutableStateFlow(false)
    var hasShownSnackbarNoGPS = false


    init {
        Log.d("HomeScreenViewModel", "Init, hentdata()")
        hentdata()
    }


    fun hentdata() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeScreenUiState.update { currentHomeUiState ->
                Log.d("ViewModel", "Henter data")
                // hent data fra reps
                Log.d("ViewModel ", "Data hentet")
                // oppdater HomeUiState med data fra reps her
                nedborRepository.updateNedbor(long, lat)
                val nedborgood = nedborRepository.regnGood()
                val skyGood = nedborRepository.skydekkeGood()
                val tokeGood = nedborRepository.tokeGood()
                val dewGood = nedborRepository.dewPointGood()
                val humGood= nedborRepository.relativHumGood()

                //Placeholder verider
                val r = nedborRepository.getMaxRegn()
                val t = nedborRepository.getMaxSiktToke()
                val s = nedborRepository.getMaxSiktSkydekke()
                val g = nedborRepository.getMaxGust()
                val d = nedborRepository.getMaxDewPoint()
                val h = nedborRepository.getMaxRelativHumidity()
                val sh = vindRepository.getMaxSheerWind()
                val vI = vindRepository.getMaxVindILufta()

                val vindOver10 = vindRepository.getDataOver10m(long, lat, vI, sh)


                val vindunder10 = nedborRepository.getDataUnder10m()
                Log.d("MSG", "${vindunder10}")
                val nedbor = nedborRepository.getNedBor3timer()
                val skydekke = nedborRepository.getSiktSkydekke3timer()
                val toke = nedborRepository.getSiktToke3timer()
                val vinderunder10Good = nedborRepository.vindunder10good()
                val dewPoint = nedborRepository.getDewPoint3timer()
                val relativeHumidity = nedborRepository.getRelativeHumidity3timer()



                viewModelScope.launch(Dispatchers.Main) {
                    _showDialog.value = true
                }
                _homeScreenUiState.value = _homeScreenUiState.value.copy(siktSkydekke = skydekke)
                _homeScreenUiState.value = _homeScreenUiState.value.copy(nedbor = nedbor)
                _homeScreenUiState.value = _homeScreenUiState.value.copy(siktToke = toke)
                currentHomeUiState.copy(
                    nedborGood = nedborgood,
                    skyGood = skyGood,
                    tokeGood = tokeGood,
                    dewGood = dewGood,
                    humGood = humGood,
                    vindUnder10 = vindunder10,
                    vindOver10 = vindOver10,
                    nedbor = nedbor,
                    siktSkydekke = skydekke,
                    siktToke = toke,
                    vindUnder10Good = vinderunder10Good,
                    long = "59.911491",
                    lat = "10.757933",
                    dewPoint = dewPoint,
                    relativeHumidity = relativeHumidity,
                    maxRegn = r,
                    maxToke = t,
                    maxSkydekke = s,
                    maxGust = g,
                    maxDewPoint = d,
                    maxRelativHumidity = h,
                    maxSheerWind = sh,
                    maxVindILufta = vI
                )
            }
        }
    }

    fun byttpos(longbyt: String, latbyt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            nedborRepository.updateNedbor(longbyt, latbyt)
            _homeScreenUiState.update { currentHomeUiState ->
                Log.d("ViewModel, byttpos()", "starer. $longbyt,$latbyt")
                long = longbyt
                lat = latbyt
                val vin10 = nedborRepository.getDataUnder10m()
                val vingood = nedborRepository.vindunder10good()
                val vindo10 = vindRepository.getDataOver10m(
                    long,
                    lat,
                    vindRepository.getMaxVindILufta(),
                    vindRepository.getMaxSheerWind()
                )
                val nedny = nedborRepository.getNedBor3timer()
                val nednygood = nedborRepository.regnGood()
                val skynygood = nedborRepository.skydekkeGood()
                val tokenygood = nedborRepository.tokeGood()
                val dewnygood = nedborRepository.dewPointGood()
                val humnygood = nedborRepository.relativHumGood()
                val nyskydekke = nedborRepository.getSiktSkydekke3timer()
                val nytaake = nedborRepository.getSiktToke3timer()
                val nyDewPoint = nedborRepository.getDewPoint3timer()
                val relativeHumidity = nedborRepository.getRelativeHumidity3timer()



                Log.d("ViewModel, byttpos() ", "posisjon byttet")
                Log.d("ViewModel, byttpos(), Ny Pos:", "${long},${lat}")

                viewModelScope.launch(Dispatchers.Main) {
                    _showDialog.value = true
                }

                currentHomeUiState.copy(
                    long = longbyt,
                    lat = latbyt,
                    vindUnder10 = vin10,
                    vindUnder10Good = vingood,
                    vindOver10 = vindo10,
                    nedbor = nedny,
                    nedborGood = nednygood,
                    siktSkydekke = nyskydekke,
                    siktToke = nytaake,
                    skyGood = skynygood,
                    tokeGood = tokenygood,
                    dewGood = dewnygood,
                    humGood = humnygood,
                    dewPoint = nyDewPoint,
                    relativeHumidity = relativeHumidity,

                    )


            }
        }
    }

    fun updateMarkerPos(latlng: LatLng) {
        viewModelScope.launch {
            _homeScreenUiState.update { currentState ->
                currentState.copy(
                    markerLatLng = latlng
                )
            }
        }
    }

    fun areaGood(value: Boolean) {
        viewModelScope.launch {
            _homeScreenUiState.update { currentState ->
                currentState.copy(
                    adrok = value.toString()
                )
            }
        }
    }

    fun updateCameraPos(cameraPosistion: CameraPosition) {
        viewModelScope.launch {
            _homeScreenUiState.update { currentState ->
                currentState.copy(cameraPosistion = cameraPosistion)
            }
        }
    }


    fun setParameter(
        regn: String, sky: String, toke: String, gust: String, dew: String, hum: String,
        sheer: String, luft: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _homeScreenUiState.update { currentHomeUiState ->
                nedborRepository.setMaxRegn(regn.toDouble())
                nedborRepository.setMaxSiktSkydekke(sky.toDouble())
                nedborRepository.setMaxSiktToke(toke.toDouble())
                nedborRepository.setMaxRelativHumidity(hum.toDouble())
                nedborRepository.setMaxDewPoint(dew.toDouble())
                nedborRepository.setMaxGust(gust.toDouble())
                vindRepository.setMaxSheerWind(sheer.toDouble())
                vindRepository.setMaxVindILufta(luft.toDouble())

                val nyregn = nedborRepository.getMaxRegn()
                val nytoke = nedborRepository.getMaxSiktToke()
                val nyskydekke = nedborRepository.getMaxSiktSkydekke()
                val nygust = nedborRepository.getMaxGust()
                val nydew = nedborRepository.getMaxDewPoint()
                val nyhum = nedborRepository.getMaxRelativHumidity()
                val nysheer = vindRepository.getMaxSheerWind()
                val nyvILuft = vindRepository.getMaxVindILufta()

                val nedborgood = nedborRepository.regnGood()
                val skyGood = nedborRepository.skydekkeGood()
                val tokeGood = nedborRepository.tokeGood()
                val dewGood = nedborRepository.dewPointGood()
                val humGood = nedborRepository.relativHumGood()
                val nyvinderunder10Good = nedborRepository.vindunder10good()

                val vindOver10 = vindRepository.getDataOver10m(long, lat, nyvILuft, nysheer)
                val vindOver10Good = vindRepository.vindOver10mgood(vindOver10)

                currentHomeUiState.copy(
                    maxRegn = nyregn,
                    maxToke = nytoke,
                    maxSkydekke = nyskydekke,
                    maxGust = nygust,
                    maxDewPoint = nydew,
                    maxRelativHumidity = nyhum,
                    maxSheerWind = nysheer,
                    maxVindILufta = nyvILuft,
                    nedborGood = nedborgood,
                    skyGood = skyGood,
                    tokeGood = tokeGood,
                    dewGood = dewGood,
                    humGood = humGood,
                    vindOver10 = vindOver10,
                    vindOver10Good = vindOver10Good,
                    vindUnder10Good = nyvinderunder10Good,
                )

            }
        }
    }

    fun setDefault() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeScreenUiState.update { currentHomeUiState ->
                nedborRepository.setMaxRegn(0.0)
                nedborRepository.setMaxSiktToke(0.0)
                nedborRepository.setMaxSiktSkydekke(15.0)
                nedborRepository.setMaxGust(8.6)
                nedborRepository.setMaxDewPoint(15.0)
                nedborRepository.setMaxRelativHumidity(75.0)
                vindRepository.setMaxSheerWind(24.5)
                vindRepository.setMaxVindILufta(17.2)
                val nyregn = nedborRepository.getMaxRegn()
                val nytoke = nedborRepository.getMaxSiktToke()
                val nyskydekke = nedborRepository.getMaxSiktSkydekke()
                val nygust = nedborRepository.getMaxGust()
                val nydew = nedborRepository.getMaxDewPoint()
                val nyhum = nedborRepository.getMaxRelativHumidity()
                val nysheer = vindRepository.getMaxSheerWind()
                val nyvILuft = vindRepository.getMaxVindILufta()

                val nednygood = nedborRepository.regnGood()
                val skynygood = nedborRepository.skydekkeGood()
                val tokenygood = nedborRepository.tokeGood()
                val dewnygood = nedborRepository.dewPointGood()
                val humnygood = nedborRepository.relativHumGood()
                val nyvinderunder10Good = nedborRepository.vindunder10good()

                val vindOver10 = vindRepository.getDataOver10m(long, lat, nyvILuft, nysheer)
                val vindOver10Good = vindRepository.vindOver10mgood(vindOver10)


                currentHomeUiState.copy(
                    maxRegn = nyregn,
                    maxToke = nytoke,
                    maxSkydekke = nyskydekke,
                    maxGust = nygust,
                    maxDewPoint = nydew,
                    maxRelativHumidity = nyhum,
                    maxSheerWind = nysheer,
                    maxVindILufta = nyvILuft,
                    nedborGood = nednygood,
                    skyGood = skynygood,
                    tokeGood = tokenygood,
                    dewGood = dewnygood,
                    humGood = humnygood,
                    vindOver10 = vindOver10,
                    vindOver10Good = vindOver10Good,
                    vindUnder10Good = nyvinderunder10Good,
                )

            }
        }
    }


}