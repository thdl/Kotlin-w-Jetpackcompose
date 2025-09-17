package no.uio.ifi.in2000.thedl.myapplication.data.vind

import no.uio.ifi.in2000.thedl.myapplication.model.VindOver10mDataklasse


class VindRepository {


    var maxSheerWind: Double = 24.5
    var maxVindILufta: Double = 17.2


    suspend fun getDataOver10m(
        lat: String,
        lon: String,
        maksVind: Double,
        maksShear: Double
    ): VindOver10mDataklasse {
        val vindDataOver10MDataSource: VindDataOver10mDataSource = VindDataOver10mDataSource()
        val result: VindOver10mDataklasse =
            vindDataOver10MDataSource.get_data_from_backendServer(lat, lon, maksVind, maksShear)

        result.vindData.forEach { (_, DataTrykkFlate) ->
            DataTrykkFlate.vindPil = hent_vind_pil(DataTrykkFlate.retning)
        }

        return result
    }

     fun vindOver10mgood(data: VindOver10mDataklasse): Boolean {
        return data.vindData.values.all { dataTrykkFlate ->
            dataTrykkFlate.shearInnenforMax && dataTrykkFlate.vindInnenforMax
        }
    }


    suspend fun setMaxSheerWind(ny: Double) {
        maxSheerWind = ny
        return
    }

    suspend fun getMaxSheerWind(): Double {
        return maxSheerWind
    }

    suspend fun setMaxVindILufta(ny: Double) {
        maxVindILufta = ny
    }

    suspend fun getMaxVindILufta(): Double {
        return maxVindILufta
    }


    fun hent_vind_pil(vinkel: Double): String {
        val piler: List<String> = listOf("⬇", "⬋", "⬅", "⬉", "⬆", "⬈", "⮕", "⬊")
        val num: Double = 45.0
        val index = ((vinkel + 22.5) % 360) / num
        return piler[index.toInt()]
    }


}
