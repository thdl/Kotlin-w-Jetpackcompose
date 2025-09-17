package no.uio.ifi.in2000.thedl.myapplication.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.thedl.myapplication.ui.homescreen.HomeScreen
import no.uio.ifi.in2000.thedl.myapplication.ui.homescreen.HomeScreenViewModel
import no.uio.ifi.in2000.thedl.myapplication.ui.mapScreen.mapScreen
import no.uio.ifi.in2000.thedl.myapplication.ui.parameterscreen.ParameterScreen

class Navigation {

    @SuppressLint("UnrememberedMutableState", "NotConstructor")
    @Composable
    fun Navigation(
        navController: NavHostController = rememberNavController(),
    ) {
        LocalContext.current.applicationContext
        val selected = remember {
            mutableStateOf(Icons.Default.Home)
        }

        Scaffold(
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.Black
                ) {
                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.Home
                            navController.navigate("home") {
                                popUpTo("home") {
                                    inclusive = true
                                    saveState = true
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray
                            )
                            Text(
                                text = "Home",
                                color = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.Check
                            navController.navigate("parameter") {
                                popUpTo("parameter") {
                                    saveState = true
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Parameter",
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.Check) Color.White else Color.DarkGray
                            )
                            Text(
                                text = "Parameters",
                                color = if (selected.value == Icons.Default.Check) Color.White else Color.DarkGray,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.LocationOn
                            navController.navigate("map") {
                                popUpTo("map") {
                                    saveState = true
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Location",
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.LocationOn) Color.White else Color.DarkGray
                            )
                            Text(
                                text = "Location",
                                color = if (selected.value == Icons.Default.LocationOn) Color.White else Color.DarkGray,
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            val homeScreenViewModel = viewModel<HomeScreenViewModel>()
            NavHost(
                navController = navController,
                startDestination = Screens.Home.screen,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screens.Home.screen) { HomeScreen(homeScreenViewModel) }
                composable(Screens.Parameter.screen) { ParameterScreen(homeScreenViewModel) }
                composable(Screens.Map.screen) { mapScreen(homeScreenViewModel) }
            }
        }
    }
}