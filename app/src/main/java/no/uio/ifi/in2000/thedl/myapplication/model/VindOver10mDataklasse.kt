package no.uio.ifi.in2000.thedl.myapplication.model

import com.google.gson.annotations.SerializedName


data class VindOver10mDataklasse(
    val vindData: Map<String, DataTrykkFlate> = emptyMap()

)

data class DataTrykkFlate(
    val meterOverHavet: Double,
    val retning: Double,
    val shearInnenforMax: Boolean,
    val shearvind: Double,
    val styrke: Double,
    val temp: Double,
    @SerializedName("u-komponent") val uKomponent: Double,
    @SerializedName("v-komponent") val vKomponent: Double,
    val vindInnenforMax: Boolean,
    var vindPil: String = ""
)