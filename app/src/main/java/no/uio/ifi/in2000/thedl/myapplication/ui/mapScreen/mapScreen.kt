package no.uio.ifi.in2000.thedl.myapplication.ui.mapScreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.thedl.myapplication.data.koordinater.koordinater
import no.uio.ifi.in2000.thedl.myapplication.ui.homescreen.HomeScreenViewModel
import no.uio.ifi.in2000.thedl.myapplication.ui.homescreen.HomeUiState
import pub.devrel.easypermissions.EasyPermissions

@SuppressLint("MissingPermission")
fun isPointInPolygon(point: LatLng, polygon: List<LatLng>): Boolean {
    var inside = false

    for (i in polygon.indices) {
        val j = (i + 1) % polygon.size
        val polyVertex1 = polygon[i]
        val polyVertex2 = polygon[j]

        if (polyVertex1.latitude > point.latitude != polyVertex2.latitude > point.latitude &&
            point.longitude < (polyVertex2.longitude - polyVertex1.longitude) * (point.latitude - polyVertex1.latitude) / (polyVertex2.latitude - polyVertex1.latitude) + polyVertex1.longitude
        ) {
            inside = !inside
        }
    }
    return inside
}


private fun updateViewModelPositionAndCheckNorway(
    latLng: LatLng,
    viewModel: HomeScreenViewModel,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    val koordinater = koordinater()
    if (isPointInPolygon(latLng, koordinater.norwayPolygon)) {
        viewModel.byttpos(latLng.latitude.toString(), latLng.longitude.toString())

    } else {
        coroutineScope.launch {
            snackbarHostState.showSnackbar("The selected position is not in Norway\nPosition will not change if not in Norway")
        }

    }
}


@SuppressLint("MissingPermission")
@Composable
fun mapScreen(
    viewModel: HomeScreenViewModel,
    context: Context = LocalContext.current
) {
    val Koordinater: koordinater = koordinater()
    val mapscreenUiState: HomeUiState by viewModel.homeScreenUiState.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = mapscreenUiState.cameraPosistion ?: CameraPosition.fromLatLngZoom(
            LatLng(
                59.911491,
                10.757933
            ), 7f
        )
    }

    val showSnackbarNoGPS = remember { mutableStateOf(false) }
    val showSnackbarHelper = remember { mutableStateOf(false) }
    var deviceLocation by remember { mutableStateOf<LatLng?>(null) }
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope2 = rememberCoroutineScope()
    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                // Permission granted, get the device's location
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            deviceLocation = LatLng(location.latitude, location.longitude)
                            updateViewModelPositionAndCheckNorway(
                                deviceLocation ?: LatLng(0.0, 0.0),
                                viewModel,
                                snackbarHostState,
                                coroutineScope2
                            )
                            // Update the map's camera position to the device's location
                            if (deviceLocation != null) {
                                val cameraUpdate =
                                    CameraUpdateFactory.newLatLngZoom(deviceLocation!!, 10f)
                                cameraPositionState.move(cameraUpdate)
                                showSnackbarHelper.value = true
                            }
                        }
                    }
            } else {
                // Permission denied, handle the case
                if (!viewModel.hasShownSnackbarNoGPS) {
                    showSnackbarNoGPS.value = true
                    viewModel.hasShownSnackbarNoGPS = true
                }

            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Permission already granted, get the device's location
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        deviceLocation = LatLng(location.latitude, location.longitude)


                    }
                }
        } else {
            // Request the location permission
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        }
    }

    val showSnackbarPolyMsg = remember { mutableStateOf(false) }
    var clickedPolygonPoints: String by remember { mutableStateOf("") }
    val markerState =
        rememberMarkerState(position = mapscreenUiState.markerLatLng ?: LatLng(0.0, 0.0))


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLongClick = { latLng ->
            markerState.position = latLng
            updateViewModelPositionAndCheckNorway(
                latLng,
                viewModel,
                snackbarHostState,
                coroutineScope2
            )
            viewModel.updateMarkerPos(latLng)
            viewModel.updateCameraPos(cameraPositionState.position)
            val isInsidePolygon = Koordinater.koordinaterListe.any { koordinater ->
                isPointInPolygon(latLng, koordinater.polygon)
            }
            if (isInsidePolygon) {
                viewModel.areaGood(isInsidePolygon)
            } else {
                viewModel.areaGood(isInsidePolygon)
            }

        }
    ) {
        Koordinater.koordinaterListe.forEach { koordinater ->
            Polygon(
                points = koordinater.polygon,
                visible = true,
                fillColor = Color.Green.copy(alpha = 0.15f),
                strokeWidth = 10.0f,
                strokeColor = Color.Green,
                clickable = true,
                onClick = {
                    clickedPolygonPoints =
                        koordinater.navn + "\n" + koordinater.klubb + "\n" + koordinater.melding + "\n"
                    showSnackbarPolyMsg.value = true

                }
            )
        }

        // Add a marker for the device's location
        deviceLocation?.let { location ->
            Marker(
                state = rememberMarkerState(position = location),
                visible = true,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                title = "Your Position",
                draggable = false,


                )
        }


        Marker(
            state = markerState,
            title = "Valgt lokasjon",
            snippet = "Marker in ${markerState.position.latitude.toInt()}, ${markerState.position.longitude.toInt()}",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
            draggable = true,
            visible = true,
        )
        Polygon(
            points = Koordinater.norwayPolygon,
            visible = false,
            fillColor = Color.Green.copy(alpha = 0.15f),
            strokeWidth = 10.0f,
            strokeColor = Color.Green,
            clickable = false,
        )
    }

    if (showSnackbarPolyMsg.value) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                androidx.compose.material3.TextButton(
                    onClick = { showSnackbarPolyMsg.value = false }
                ) {
                    androidx.compose.material3.Text("Close")
                }
            }
        ) {
            androidx.compose.material3.Text(text = clickedPolygonPoints)
        }
    }
    if (showSnackbarNoGPS.value) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                androidx.compose.material3.TextButton(
                    onClick = { showSnackbarNoGPS.value = false }
                ) {
                    androidx.compose.material3.Text("Close")
                }
            }
        ) {
            androidx.compose.material3.Text(text = "No GPS signal found\nCurrent chosen location: ${mapscreenUiState.long} ${mapscreenUiState.lat}\nChange location by long press on the map")
        }
    }
    if (showSnackbarHelper.value) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                androidx.compose.material3.TextButton(
                    onClick = { showSnackbarHelper.value = false }
                ) {
                    androidx.compose.material3.Text("Close")
                }
            }
        ) {
            androidx.compose.material3.Text(text = "Change location by long press on the map")
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp)
    )
}
