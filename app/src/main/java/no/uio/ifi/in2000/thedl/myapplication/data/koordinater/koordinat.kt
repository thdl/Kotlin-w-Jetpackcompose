package no.uio.ifi.in2000.thedl.myapplication.data.koordinater

import com.google.android.gms.maps.model.LatLng

data class koordinat(
    val polygon: List<LatLng>,
    val navn: String,
    val klubb: String,
    val melding: String,
)