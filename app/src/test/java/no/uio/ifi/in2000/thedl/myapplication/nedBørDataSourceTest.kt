package no.uio.ifi.in2000.thedl.myapplication

import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import kotlinx.coroutines.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.thedl.myapplication.data.nedbor.nedborDataSource
import no.uio.ifi.in2000.thedl.myapplication.model.Geometry
import no.uio.ifi.in2000.thedl.myapplication.model.Meta
import no.uio.ifi.in2000.thedl.myapplication.model.NedBorResponse
import no.uio.ifi.in2000.thedl.myapplication.model.Properties
import no.uio.ifi.in2000.thedl.myapplication.model.Units

class nedBorDataSourceTest{

    suspend fun nedBorGet() : NedBorResponse? {

        val nedborDataSource1 = nedborDataSource()
        val result : NedBorResponse? = nedborDataSource1.getNedbor1()

        return result

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getNedbor_NotEmptyResponse() = runTest {

        val result = nedBorGet()
        advanceUntilIdle()

        assertTrue(result!=null)
    }

    @Test
    fun getNedbor_MakeFilledDataClass() = runTest {

        val client = mockk<nedborDataSource>()

        coEvery { client.getNedbor("11.0","60.0", "1000.0") } returns NedBorResponse(
            Geometry(listOf(11.0, 60.0, 1000.0), type = "Point"),
            Properties(
                Meta(
                    Units(
                        "hPa",
                        "C",
                        "C",
                        "C",
                        "%",
                        "%",
                        "%",
                        "%",
                        "C",
                        "%",
                        "C",
                        "%",
                        "mm",
                        "mm",
                        "mm",
                        "%",
                        "%",
                        "%",
                        "1",
                        "degrees",
                        "m/s",
                        "m/s",
                        "m/s",
                        "m/s"
                    ), ""
                ), emptyList()
            ),
            "Point"
        )

        val mockResponse = NedBorResponse(
            Geometry(listOf(11.0, 60.0, 1000.0), type = "Point"),
            Properties(
                Meta(
                    Units(
                        "hPa",
                        "C",
                        "C",
                        "C",
                        "%",
                        "%",
                        "%",
                        "%",
                        "C",
                        "%",
                        "C",
                        "%",
                        "mm",
                        "mm",
                        "mm",
                        "%",
                        "%",
                        "%",
                        "1",
                        "degrees",
                        "m/s",
                        "m/s",
                        "m/s",
                        "m/s"
                    ), ""
                ), emptyList()
            ),
            "Point"
        )

        val result = client.getNedbor("11.0","60.0", "1000.0")

        assertEquals(mockResponse, result)
    }

}


