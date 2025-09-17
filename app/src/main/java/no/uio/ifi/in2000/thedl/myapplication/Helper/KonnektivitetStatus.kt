package no.uio.ifi.in2000.thedl.myapplication.Helper

sealed class KonnektivitetStatus {
    object Available : KonnektivitetStatus()
    object Unavailable : KonnektivitetStatus()

}