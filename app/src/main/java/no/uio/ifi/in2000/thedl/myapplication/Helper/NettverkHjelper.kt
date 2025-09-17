package no.uio.ifi.in2000.thedl.myapplication.Helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

val Context.currentConnectivityStatus: KonnektivitetStatus
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityStatus(connectivityManager)
    }

private fun getCurrentConnectivityStatus(
    connectivityManager: ConnectivityManager
): KonnektivitetStatus {
    val connected = connectivityManager.allNetworks.any { network ->
        connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false

    }

    return if (connected) {

        KonnektivitetStatus.Available
    } else {

        KonnektivitetStatus.Unavailable
    }
}


fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = NetworkCallback { connectionState -> trySend(connectionState) }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    val currentState = getCurrentConnectivityStatus(connectivityManager)
    trySend(currentState)

    awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
    }

}


fun NetworkCallback(callback: (KonnektivitetStatus) -> Unit): NetworkCallback {
    return object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(KonnektivitetStatus.Available)
        }

        override fun onLost(network: Network) {
            callback(KonnektivitetStatus.Unavailable)
        }

    }
}