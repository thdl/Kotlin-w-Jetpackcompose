package no.uio.ifi.in2000.thedl.myapplication.Helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import no.uio.ifi.in2000.thedl.myapplication.ui.homescreen.BaseLayout
import no.uio.ifi.in2000.thedl.myapplication.ui.navigation.Navigation


@ExperimentalCoroutinesApi
@Composable
fun checkConnectivityStatus(): Boolean {

    val connection by connectivityStatus()

    val isConnected = connection === KonnektivitetStatus.Available

    return isConnected
}


@ExperimentalCoroutinesApi
@Composable
fun connectivityStatus(): State<KonnektivitetStatus> {
    val mCtx = LocalContext.current

    return produceState(initialValue = mCtx.currentConnectivityStatus) {
        mCtx.observeConnectivityAsFlow().collect { value = it }

    }

}


@Composable
fun MainContent() {

    if (checkConnectivityStatus()) {

        Navigation().Navigation()

    } else if(!checkConnectivityStatus()) {

        BaseLayout(
            null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null
        )
    }else{

        BaseLayout(
            null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null
        )
    }


}