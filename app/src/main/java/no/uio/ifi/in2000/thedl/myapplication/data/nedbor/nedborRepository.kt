package no.uio.ifi.in2000.thedl.myapplication.data.nedbor

import android.util.Log
import no.uio.ifi.in2000.thedl.myapplication.model.NedBorResponse

interface nedborRepository {

    suspend fun updateNedbor(long: String, lat: String)
    suspend fun getNedbor(): Double
    suspend fun getSiktSkydekke(): Double

    suspend fun getSiktToke(): Double

    suspend fun regnGood(): Boolean

    suspend fun tokeGood(): Boolean

    suspend fun skydekkeGood(): Boolean

    suspend fun dewPointGood(): Boolean

    suspend fun relativHumGood(): Boolean

    suspend fun getDewPoint(): Double

    suspend fun getRelativeHumidity(): Double

    suspend fun setMaxRegn(regn: Double)
    suspend fun setMaxSiktToke(toke: Double)
    suspend fun setMaxSiktSkydekke(sky: Double)

    suspend fun setMaxDewPoint(dew: Double)

    suspend fun setMaxRelativHumidity(humidity: Double)

    suspend fun getMaxRegn(): Double

    suspend fun getMaxSiktSkydekke(): Double

    suspend fun getMaxSiktToke(): Double

    suspend fun getMaxDewPoint(): Double

    suspend fun getMaxRelativHumidity(): Double

    suspend fun setMaxGust(ny: Double)
    suspend fun getMaxGust(): Double

    suspend fun getDataUnder10m(): MutableList<MutableList<String>>

    suspend fun vindunder10good(): Boolean

    suspend fun getVindUnder10Double(): Double

    fun hent_vind_pil(vinkel: Double): String

    suspend fun getNedBor3timer(): MutableList<MutableList<String>>

    suspend fun getSiktSkydekke3timer(): MutableList<MutableList<String>>

    suspend fun getSiktToke3timer(): MutableList<MutableList<String>>

    suspend fun getDewPoint3timer(): MutableList<MutableList<String>>

    suspend fun getRelativeHumidity3timer(): MutableList<MutableList<String>>


}

class nedborRepositoryImplementation(
    private val nedborDataSource: nedborDataSource = nedborDataSource()

) : nedborRepository {
    var maxRegn: Double = 0.0
    var maxSiktToke: Double = 0.0
    var maxSiktSkydekke: Double = 15.0
    var maxDewPoint: Double = 15.0
    var maxRelativHumidity: Double = 75.0
    var maxGust: Double = 8.6

    private lateinit var nedBorResponse: NedBorResponse

    override suspend fun updateNedbor(long: String, lat: String) {
        nedBorResponse = nedborDataSource.getNedborNoAlt(long, lat)

    }

    override suspend fun getNedbor(): Double {

        return nedBorResponse.properties.timeseries[0].data.next_1_hours.details.precipitation_amount
    }

    override suspend fun getNedBor3timer(): MutableList<MutableList<String>> {

        val list: MutableList<MutableList<String>> = mutableListOf()

        for (i in 0..2) {
            list.add(
                mutableListOf(
                    nedBorResponse.properties.timeseries[i].time,
                    nedBorResponse.properties.timeseries[i].data.next_1_hours.details.precipitation_amount.toString()
                )
            )
            Log.d(
                "precipitation_amount:",
                nedBorResponse.properties.timeseries[i].data.next_1_hours.details.precipitation_amount.toString()
            )
        }
        Log.d("geNedBor3timer:", list.toString())
        return list
    }

    override suspend fun getSiktSkydekke(): Double {

        return nedBorResponse.properties.timeseries[0].data.instant.details.cloud_area_fraction
    }

    override suspend fun getSiktSkydekke3timer(): MutableList<MutableList<String>> {

        val list: MutableList<MutableList<String>> = mutableListOf<MutableList<String>>()

        for (i in 0..2) {
            list.add(
                mutableListOf(
                    nedBorResponse.properties.timeseries[i].time,
                    nedBorResponse.properties.timeseries[i].data.instant.details.cloud_area_fraction.toString()
                )
            )
            Log.d(
                "cloud_area_fraction:",
                nedBorResponse.properties.timeseries[i].data.instant.details.cloud_area_fraction.toString()
            )
        }
        Log.d("getSiktSkydekke3timer:", list.toString())
        return list
    }

    override suspend fun getSiktToke(): Double {

        return nedBorResponse.properties.timeseries[0].data.instant.details.fog_area_fraction

    }

    override suspend fun getSiktToke3timer(): MutableList<MutableList<String>> {

        val list: MutableList<MutableList<String>> = mutableListOf()

        for (i in 0..2) {
            list.add(
                mutableListOf(
                    nedBorResponse.properties.timeseries[i].time,
                    nedBorResponse.properties.timeseries[i].data.instant.details.fog_area_fraction.toString()
                )
            )
            Log.d(
                "fog_area_fraction:",
                nedBorResponse.properties.timeseries[i].data.instant.details.fog_area_fraction.toString()
            )
        }
        Log.d("getSiktToke3timer:", list.toString())
        return list
    }

    override suspend fun getDewPoint(): Double {

        return nedBorResponse.properties.timeseries[0].data.instant.details.dew_point_temperature
    }

    override suspend fun getDewPoint3timer(): MutableList<MutableList<String>> {

        val list: MutableList<MutableList<String>> = mutableListOf()

        for (i in 0..2) {
            list.add(
                mutableListOf(
                    nedBorResponse.properties.timeseries[i].time,
                    nedBorResponse.properties.timeseries[i].data.instant.details.dew_point_temperature.toString()
                )
            )
            Log.d(
                "dew_point_temperature:",
                nedBorResponse.properties.timeseries[i].data.instant.details.dew_point_temperature.toString()
            )
        }
        Log.d("getDewPoint3timer:", list.toString())
        return list
    }

    override suspend fun getRelativeHumidity(): Double {

        return nedBorResponse.properties.timeseries[0].data.instant.details.relative_humidity
    }

    override suspend fun getRelativeHumidity3timer(): MutableList<MutableList<String>> {

        val list: MutableList<MutableList<String>> = mutableListOf()

        for (i in 0..2) {
            list.add(
                mutableListOf(
                    nedBorResponse.properties.timeseries[i].time,
                    nedBorResponse.properties.timeseries[i].data.instant.details.relative_humidity.toString()
                )
            )
            Log.d(
                "relative_humidity:",
                nedBorResponse.properties.timeseries[i].data.instant.details.relative_humidity.toString()
            )
        }
        Log.d("getRelativeHumidity3timer:", list.toString())
        return list
    }

    override suspend fun regnGood(): Boolean {
        val regn: Double = getNedbor()

        if (regn != null) {
            return regn <= maxRegn
        }
        return false
    }

    override suspend fun tokeGood(): Boolean {

        val sikttoke = getSiktToke()

        if (sikttoke != null) {
            if (sikttoke <= maxSiktToke) {
                return true
            }
        }
        return false

    }

    override suspend fun skydekkeGood(): Boolean {
        val siktsky = getSiktSkydekke()

        if (siktsky != null) {
            if (siktsky <= maxSiktSkydekke) {
                return true
            }
        }
        return false
    }

    override suspend fun dewPointGood(): Boolean {
        val dew = getDewPoint()

        if (dew != null) {
            if (dew <= maxDewPoint) {
                return true
            }
        }
        return false
    }

    override suspend fun relativHumGood(): Boolean {
        val hum = getRelativeHumidity()

        if (hum != null) {
            if (hum <= maxRelativHumidity) {
                return true
            }
        }
        return false
    }


    override suspend fun setMaxRegn(regn: Double) {
        maxRegn = regn
    }

    override suspend fun setMaxSiktToke(toke: Double) {
        maxSiktToke = toke
        return
    }

    override suspend fun setMaxSiktSkydekke(sky: Double) {
        maxSiktSkydekke = sky
        return
    }

    override suspend fun setMaxDewPoint(dew: Double) {
        maxDewPoint = dew
        return
    }

    override suspend fun setMaxRelativHumidity(humidity: Double) {
        maxRelativHumidity = humidity
        return
    }

    override suspend fun getMaxRegn(): Double {
        return maxRegn
    }


    override suspend fun getMaxSiktSkydekke(): Double {
        return maxSiktSkydekke
    }

    override suspend fun getMaxSiktToke(): Double {
        return maxSiktToke
    }

    override suspend fun getMaxDewPoint(): Double {
        return maxDewPoint
    }

    override suspend fun getMaxRelativHumidity(): Double {
        return maxRelativHumidity
    }

    override suspend fun setMaxGust(ny: Double) {
        maxGust = ny
    }

    override suspend fun getMaxGust(): Double {
        return maxGust
    }

    override suspend fun getDataUnder10m(): MutableList<MutableList<String>> {


        val list: MutableList<MutableList<String>> = mutableListOf()

        for (i in 0..2) {//neste 3 timene
            list.add(
                mutableListOf(
                    nedBorResponse.properties.timeseries[i].time,
                    nedBorResponse.properties.timeseries[i].data.instant.details.wind_speed_of_gust.toString(),
                    nedBorResponse.properties.timeseries[i].data.instant.details.wind_from_direction.toString(),
                    hent_vind_pil(nedBorResponse.properties.timeseries[i].data.instant.details.wind_from_direction)
                )
            )
            Log.d(
                "windSpeedofgust:",
                nedBorResponse.properties.timeseries[i].data.instant.details.wind_speed_of_gust.toString()
            )
        }
        Log.d("getDataUnder10:", list.toString())
        return list
    }

    override suspend fun vindunder10good(): Boolean {
        var sum = getVindUnder10Double()
        return sum / 3 < maxGust
    }

    override suspend fun getVindUnder10Double(): Double {
        val list: MutableList<MutableList<String>> = getDataUnder10m()
        var sum = list[0][1].toDouble() + list[1][1].toDouble() + list[2][1].toDouble()
        sum = sum / 3
        Log.d("toDouble : sum", sum.toString())
        return sum
    }

    override fun hent_vind_pil(vinkel: Double): String {
        val piler: List<String> = listOf("⬇", "⬋", "⬅", "⬉", "⬆", "⬈", "⮕", "⬊")
        val num: Double = 45.0
        val index = ((vinkel + 22.5) % 360) / num
        return piler[index.toInt()]
    }

}


