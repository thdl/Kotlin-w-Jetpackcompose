package no.uio.ifi.in2000.thedl.myapplication.data.nedbor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import io.ktor.util.appendIfNameAbsent
import no.uio.ifi.in2000.thedl.myapplication.model.NedBorResponse


class nedborDataSource {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
        defaultRequest {
            url("https://gw-uio.intark.uh-it.no/in2000/")
            headers.appendIfNameAbsent("X-Gravitee-API-Key", "d5d9e605-34bf-46d0-9cb7-0754853807c2")
        }
    }

    suspend fun getNedbor(lat: String, lon: String, altitude: String): NedBorResponse {


        val response =
            client.get("weatherapi/locationforecast/2.0/complete?altitude=$altitude&lat=$lat&lon=$lon")

        return response.body()
    }

    suspend fun getNedborNoAlt(lat: String, lon: String): NedBorResponse {


        val response = client.get("weatherapi/locationforecast/2.0/complete?&lat=$lat&lon=$lon")

        return response.body()
    }


    suspend fun getNedbor1(): NedBorResponse? {

        val response =
            client.get("weatherapi/locationforecast/2.0/complete?altitude=1000&lat=60&lon=11")

        return response.body()
    }

}



