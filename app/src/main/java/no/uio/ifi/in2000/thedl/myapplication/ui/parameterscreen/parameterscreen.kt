package no.uio.ifi.in2000.thedl.myapplication.ui.parameterscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.thedl.myapplication.Helper.checkConnectivityStatus
import no.uio.ifi.in2000.thedl.myapplication.R
import no.uio.ifi.in2000.thedl.myapplication.ui.homescreen.HomeScreenViewModel
import no.uio.ifi.in2000.thedl.myapplication.ui.homescreen.HomeUiState

private fun String.isDoubleValue(): Boolean {
    return try {
        this.toDouble()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

@SuppressLint("MissingPermission")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun ParameterScreen(
    viewModel: HomeScreenViewModel,
) {


    val parameterScreenUiState: HomeUiState by viewModel.homeScreenUiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var maxRegn = parameterScreenUiState.maxRegn
    var textRegn by remember { mutableStateOf("$maxRegn") }

    var maxToke = parameterScreenUiState.maxToke
    var textToke by remember { mutableStateOf("$maxToke") }

    var maxSkydekke = parameterScreenUiState.maxSkydekke
    var textSkydekke by remember { mutableStateOf("$maxSkydekke") }

    var maxGust = parameterScreenUiState.maxGust
    var textGust by remember { mutableStateOf("$maxGust") }

    var maxDewPoint = parameterScreenUiState.maxDewPoint
    var textDewPoint by remember { mutableStateOf("$maxDewPoint") }

    var maxRelativHumidity = parameterScreenUiState.maxRelativHumidity
    var textRelativHumidity by remember { mutableStateOf("$maxRelativHumidity") }

    var maxSheerWind = parameterScreenUiState.maxSheerWind
    var textSheerWind by remember { mutableStateOf("$maxSheerWind") }

    var maxVindILufta = parameterScreenUiState.maxVindILufta
    var textVindILufta by remember { mutableStateOf("$maxVindILufta") }

    val showSnackbarError = remember { mutableStateOf(false) }


    var textLys1 by remember {
        mutableStateOf(false)
    }

    var textLys2 by remember {
        mutableStateOf(false)
    }


    var color1: Color by remember {
        mutableStateOf(Color.LightGray)
    }
    var color2: Color by remember {
        mutableStateOf(Color.LightGray)
    }
    var color3: Color by remember {
        mutableStateOf(Color.LightGray)
    }
    var color4: Color by remember {
        mutableStateOf(Color.LightGray)
    }
    var color5: Color by remember {
        mutableStateOf(Color.LightGray)
    }
    var color6: Color by remember {
        mutableStateOf(Color.LightGray)
    }
    var color7: Color by remember {
        mutableStateOf(Color.LightGray)
    }
    var color8: Color by remember {
        mutableStateOf(Color.LightGray)
    }

    //Bølgeefekt
    LaunchedEffect(textLys1) {
        if (textLys1) {
            color1 = Color.Black
            delay(100)
            color1 = Color.LightGray

            color2 = Color.Black
            delay(100)
            color2 = Color.LightGray

            color3 = Color.Black
            delay(100)
            color3 = Color.LightGray

            color4 = Color.Black
            delay(100)
            color4 = Color.LightGray

            color5 = Color.Black
            delay(100)
            color5 = Color.LightGray

            color6 = Color.Black
            delay(100)
            color6 = Color.LightGray

            color7 = Color.Black
            delay(100)
            color7 = Color.LightGray

            color8 = Color.Black
            delay(100)
            color8 = Color.LightGray

            textLys1 = false
        }
    }

    //Bølgeefekt
    LaunchedEffect(textLys2) {
        if (textLys2) {

            color8 = Color.Black
            delay(100)
            color8 = Color.LightGray

            color7 = Color.Black
            delay(100)
            color7 = Color.LightGray

            color6 = Color.Black
            delay(100)
            color6 = Color.LightGray

            color5 = Color.Black
            delay(100)
            color5 = Color.LightGray

            color4 = Color.Black
            delay(100)
            color4 = Color.LightGray

            color3 = Color.Black
            delay(100)
            color3 = Color.LightGray

            color2 = Color.Black
            delay(100)
            color2 = Color.LightGray

            color1 = Color.Black
            delay(100)
            color1 = Color.LightGray

            textLys2 = false
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        item {
            CenterAlignedTopAppBar(
                title = {
                    if (isSystemInDarkTheme()) {
                        Image(
                            modifier = Modifier
                                .fillMaxHeight(0.11f)
                                .fillMaxWidth(0.20f)
                                .padding(5.dp),
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(id = R.drawable.logo_dark),
                            contentDescription = "Logo dark",
                        )
                    } else {
                        Image(
                            modifier = Modifier
                                .fillMaxHeight(0.11f)
                                .fillMaxWidth(0.20f)
                                .padding(5.dp),
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(id = R.drawable.logo_light),
                            contentDescription = "Logo light",
                        )
                    }
                },
                actions = {
                    checkConnectivityStatus()
                },
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier.semantics { this.contentDescription = "maxRegn" },
                value = textRegn,
                onValueChange = {
                    if (it.isDoubleValue()) {
                        textRegn = it
                    } else if (it.isEmpty()) {
                        textRegn = ""
                    } else {
                        showSnackbarError.value = true

                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                label = { Text("Maksimal nedbørsgrense :") },
                suffix = { Text(text = "mm", color = MaterialTheme.colorScheme.onSurface) },

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = color1,
                    unfocusedBorderColor = color1,
                    unfocusedLabelColor = color1,
                ),

                )
        }
        item {
            OutlinedTextField(
                modifier = Modifier.semantics { this.contentDescription = "maxSkydekke" },
                value = textSkydekke,
                onValueChange = {
                    if (it.isDoubleValue()) {
                        textSkydekke = it
                    } else if (it.isEmpty()) {
                        textSkydekke = ""
                    } else {
                        showSnackbarError.value = true

                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                label = { Text("Maksimalt skydekke i prosent :") },
                suffix = { Text(text = "%", color = MaterialTheme.colorScheme.onSurface) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = color2,
                    unfocusedBorderColor = color2,
                    unfocusedLabelColor = color2,
                ),
            )

        }
        item {
            OutlinedTextField(
                modifier = Modifier.semantics { this.contentDescription = "maxVindILufta" },
                value = textVindILufta,
                onValueChange = {
                    if (it.isDoubleValue()) {
                        textVindILufta = it
                    } else if (it.isEmpty()) {
                        textVindILufta = ""
                    } else {
                        showSnackbarError.value = true

                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                label = { Text("Maksimal vind i lufta i m/s :") },
                suffix = { Text(text = "m/s", color = MaterialTheme.colorScheme.onSurface) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = color3,
                    unfocusedBorderColor = color3,
                    unfocusedLabelColor = color3,
                ),
            )

        }
        item {
            OutlinedTextField(
                modifier = Modifier.semantics { this.contentDescription = "maxGust" },
                value = textGust,
                onValueChange = {
                    if (it.isDoubleValue()) {
                        textGust = it
                    } else if (it.isEmpty()) {
                        textGust = ""
                    } else {
                        showSnackbarError.value = true

                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                label = { Text("Maksimal vind på bakkenivå i m/s :") },
                suffix = { Text(text = "m/s", color = MaterialTheme.colorScheme.onSurface) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = color4,
                    unfocusedBorderColor = color4,
                    unfocusedLabelColor = color4,
                ),
            )

        }
        item {
            OutlinedTextField(
                modifier = Modifier.semantics { this.contentDescription = "maxSheerWind" },
                value = textSheerWind,
                onValueChange = {
                    if (it.isDoubleValue()) {
                        textSheerWind = it
                    } else if (it.isEmpty()) {
                        textSheerWind = ""
                    } else {
                        showSnackbarError.value = true

                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                label = { Text("Maksimal shear vind i m/s :") },
                suffix = { Text(text = "m/s", color = MaterialTheme.colorScheme.onSurface) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = color5,
                    unfocusedBorderColor = color5,
                    unfocusedLabelColor = color5,
                ),
            )

        }
        item {
            OutlinedTextField(
                modifier = Modifier.semantics { this.contentDescription = "maxTåke" },
                value = textToke,
                onValueChange = {
                    if (it.isDoubleValue()) {
                        textToke = it
                    } else if (it.isEmpty()) {
                        textToke = ""
                    } else {
                        showSnackbarError.value = true

                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                label = { Text("Maksimal tåkegrense i prosent :") },
                suffix = { Text(text = "%", color = MaterialTheme.colorScheme.onSurface) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = color6,
                    unfocusedBorderColor = color6,
                    unfocusedLabelColor = color6,
                ),
            )

        }
        item {
            OutlinedTextField(
                modifier = Modifier.semantics { this.contentDescription = "maxDewPoint" },
                value = textDewPoint,
                onValueChange = {
                    if (it.isDoubleValue()) {
                        textDewPoint = it
                    } else if (it.isEmpty()) {
                        textDewPoint = ""
                    } else {
                        showSnackbarError.value = true

                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                label = { Text("Maksimal dew point i grader:") },
                suffix = { Text(text = "℃", color = MaterialTheme.colorScheme.onSurface) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = color7,
                    unfocusedBorderColor = color7,
                    unfocusedLabelColor = color7,
                ),
            )

        }
        item {
            OutlinedTextField(
                modifier = Modifier.semantics { this.contentDescription = "maxRelativHumidity" },
                value = textRelativHumidity,
                onValueChange = {
                    if (it.isDoubleValue()) {
                        textRelativHumidity = it
                    } else if (it.isEmpty()) {
                        textRelativHumidity = ""
                    } else {
                        showSnackbarError.value = true

                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                label = { Text("Maksimal luftfuktighetsgrense i prosent :") },
                suffix = { Text(text = "%", color = MaterialTheme.colorScheme.onSurface) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = color8,
                    unfocusedBorderColor = color8,
                    unfocusedLabelColor = color8,
                ),
            )

        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {

                Button(
                    onClick = {
                        textLys2 = true
                        viewModel.setDefault()
                        textRegn = "0.0"
                        textSkydekke = "15.0"
                        textToke = "0.0"
                        textGust = "8.6"
                        textDewPoint = "15.0"
                        textRelativHumidity = "75.0"
                        textSheerWind = "24.5"
                        textVindILufta = "17.2"

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.semantics { this.contentDescription = "Set Default" }
                )
                { Text("Tilbakestill") }


                Button(
                    onClick =
                    {
                        textLys1 = true

                        if (textRegn.isEmpty()) {     //sjekk om textvalue er tomt
                            textRegn = "0"
                        }
                        if (textSkydekke.isEmpty()) {
                            textSkydekke = "0"
                        }
                        if (textToke.isEmpty()) {
                            textToke = "0"
                        }
                        if (textGust.isEmpty()) {
                            textGust = "0"
                        }
                        if (textDewPoint.isEmpty()) {
                            textDewPoint = "0"
                        }
                        if (textRelativHumidity.isEmpty()) {
                            textRelativHumidity = "0"
                        }
                        if (textSheerWind.isEmpty()) {
                            textSheerWind = "0"
                        }
                        if (textVindILufta.isEmpty()) {
                            textVindILufta = "0"
                        }
                        viewModel.setParameter(
                            textRegn,
                            textSkydekke,
                            textToke,
                            textGust,
                            textDewPoint,
                            textRelativHumidity,
                            textSheerWind,
                            textVindILufta
                        )

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.semantics { this.contentDescription = "Save changes" }
                )
                { Text("Lagre endringer") }
            }

        }
    };if (showSnackbarError.value) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(
                    onClick = { showSnackbarError.value = false }
                ) {
                    Text("Close")
                }
            }
        ) {
            Text(text = "Input has to be a number or dot \nExample: 3.6")
        }
    }
}

