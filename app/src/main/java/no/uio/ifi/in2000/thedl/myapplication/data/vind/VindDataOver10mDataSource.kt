package no.uio.ifi.in2000.thedl.myapplication.data.vind

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.thedl.myapplication.model.DataTrykkFlate
import no.uio.ifi.in2000.thedl.myapplication.model.VindOver10mDataklasse


class VindDataOver10mDataSource {
    suspend fun get_data_from_backendServer(
        lat: String,
        lon: String,
        maksVind: Double,
        maksShear: Double
    ): VindOver10mDataklasse {
        return try {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    gson()
                }

            }

            val latFormat = String.format("%.2f", lat.toDouble())
            val lonFormat = String.format("%.2f", lon.toDouble())


            val server_URL =
                "http://rondestvedt.pythonanywhere.com/check-wind-conditions?lat=$latFormat&lon=$lonFormat&maks_vind=$maksVind&maks_shear=$maksShear"


            val response: HttpResponse = client.get(server_URL)
            val rawJson: String = response.bodyAsText()
            Log.d("RAW", "Raw json: $rawJson")

            val gson = Gson()
            val jsonObj = gson.fromJson(rawJson, JsonObject::class.java)
            val dataMap = jsonObj.entrySet().associate {
                it.key to gson.fromJson(it.value, DataTrykkFlate::class.java)
            }
            val vindData = VindOver10mDataklasse(dataMap)
            Log.d("Dest", "Deserialisert objekt: $vindData")
            return vindData


        } catch (e: Exception) {
            val vindDataEksempel = VindOver10mDataklasse(
                vindData = mapOf(
                    "150" to DataTrykkFlate(
                        meterOverHavet = 5893.72,
                        retning = 223.38,
                        shearInnenforMax = true,
                        shearvind = 4.66,
                        styrke = 20.38,
                        temp = 220.58,
                        uKomponent = 13.99,
                        vKomponent = 14.81,
                        vindInnenforMax = false
                    ),
                    "200" to DataTrykkFlate(
                        meterOverHavet = 5563.17,
                        retning = 209.46,
                        shearInnenforMax = true,
                        shearvind = 7.1,
                        styrke = 27.48,
                        temp = 218.21,
                        uKomponent = 13.51,
                        vKomponent = 23.92,
                        vindInnenforMax = false
                    )

                )
            )
            return vindDataEksempel
        }
    }


}

