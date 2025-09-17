package no.uio.ifi.in2000.thedl.myapplication.ui.homescreen

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
) {

    val homescreenUiState: HomeUiState by viewModel.homeScreenUiState.collectAsState()
    val vindUnder10 = homescreenUiState.vindUnder10
    val vindunder10Good = homescreenUiState.vindUnder10Good
    val vindover10 = homescreenUiState.vindOver10
    val vindOver10Good = homescreenUiState.vindOver10Good
    val regn = homescreenUiState.nedbor
    val sky = homescreenUiState.siktSkydekke
    val toke = homescreenUiState.siktToke
    val dewPoint = homescreenUiState.dewPoint
    val relativeHumidity = homescreenUiState.relativeHumidity

    val nedborGood = homescreenUiState.nedborGood
    val skyGood = homescreenUiState.skyGood
    val tokeGood = homescreenUiState.tokeGood
    val dewGood = homescreenUiState.dewGood
    val humGood = homescreenUiState.humGood
    val adrok = homescreenUiState.adrok

    BaseLayout(
        viewModel,
        regn,
        sky,
        toke,
        dewPoint,
        relativeHumidity,
        nedborGood,
        skyGood,
        tokeGood,
        dewGood,
        humGood,
        vindover10,
        vindUnder10,
        vindunder10Good,
        vindOver10Good,
        adrok,
    )
}

