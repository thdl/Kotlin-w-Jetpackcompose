package no.uio.ifi.in2000.thedl.myapplication.ui.navigation

sealed class Screens(val screen: String) {
    data object Home : Screens("home")
    data object Parameter : Screens("parameter")
    data object Map : Screens("map")


}