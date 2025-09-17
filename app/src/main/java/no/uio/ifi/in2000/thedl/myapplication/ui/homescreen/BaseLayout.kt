package no.uio.ifi.in2000.thedl.myapplication.ui.homescreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.thedl.myapplication.Helper.checkConnectivityStatus
import no.uio.ifi.in2000.thedl.myapplication.R
import no.uio.ifi.in2000.thedl.myapplication.model.DataTrykkFlate
import no.uio.ifi.in2000.thedl.myapplication.model.VindOver10mDataklasse
import kotlin.math.round


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BaseLayout(
    viewModel: HomeScreenViewModel?,
    regn: MutableList<MutableList<String>>?,
    sky: MutableList<MutableList<String>>?,
    toke: MutableList<MutableList<String>>?,
    dew: MutableList<MutableList<String>>?,
    hum: MutableList<MutableList<String>>?,
    regnG: Boolean?,
    skyG: Boolean?,
    tokeG: Boolean?,
    dewG: Boolean?,
    humG: Boolean?,
    vindOver10: VindOver10mDataklasse?,
    vindUnder10: MutableList<MutableList<String>>?,
    vindUnder10Good: Boolean?,
    vindOver10Good: Boolean?,
    adrok: String?
) {
    val vindOver10: VindOver10mDataklasse? = vindOver10
    val vindUnder10: MutableList<MutableList<String>>? = vindUnder10

    val nedbor: Double? = regn?.get(0)?.get(1)?.toDouble()
    val nedbor2: Double? = regn?.get(1)?.get(1)?.toDouble()
    val nedbor3: Double? = regn?.get(2)?.get(1)?.toDouble()

    val siktSkydekke: Double? = sky?.get(0)?.get(1)?.toDouble()
    val siktSkydekke2: Double? = sky?.get(1)?.get(1)?.toDouble()
    val siktSkydekke3: Double? = sky?.get(2)?.get(1)?.toDouble()

    val siktToke: Double? = toke?.get(0)?.get(1)?.toDouble()
    val siktToke2: Double? = toke?.get(1)?.get(1)?.toDouble()
    val siktToke3: Double? = toke?.get(2)?.get(1)?.toDouble()

    val dewPoint: Double? = dew?.get(0)?.get(1)?.toDouble()
    val dewPoint2: Double? = dew?.get(1)?.get(1)?.toDouble()
    val dewPoint3: Double? = dew?.get(2)?.get(1)?.toDouble()

    val relativeHumidity: Double? = hum?.get(0)?.get(1)?.toDouble()
    val relativeHumidity2: Double? = hum?.get(1)?.get(1)?.toDouble()
    val relativeHumidity3: Double? = hum?.get(2)?.get(1)?.toDouble()

    val nedborGood: Boolean? = regnG
    val skyGood: Boolean? = skyG
    val tokeGood: Boolean? = tokeG
    val dewGood: Boolean? = dewG
    val humGood: Boolean? = humG
    val vindUnder10Good = vindUnder10Good
    val vindOver10Good: Boolean? = vindOver10Good


    //Pullrefresh
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        isRefreshing = true
        if (viewModel != null) {
            viewModel.hentdata()
        }

    })

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(2000)
            isRefreshing = false
        }
    }

    //for vindraden
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical)
//        .verticalScroll(rememberScrollState())
            .pullRefresh(state),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom

            ) {
                if (!isExpanded) {
                    Box(
                        modifier = Modifier.width(70.dp)
                    ) {
                        IconButton(onClick = {
                            if (viewModel != null) {
                                isRefreshing = true
                                viewModel.hentdata()
                            }
                        }) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_reloade),
                                contentDescription = "Oppdater",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.width(70.dp)
                    ) {
                        if (isSystemInDarkTheme()) {
                            Image(
                                modifier = Modifier
                                    .size(60.dp)
                                    .zIndex(1f)
                                    .fillMaxHeight(0.14f)
                                    .fillMaxWidth(0.24f)
                                    .absolutePadding(0.dp, 15.dp, 0.dp, 0.dp),

                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(id = R.drawable.logo_dark),
                                contentDescription = "Logo dark",
                            ) // Full bilde
                        } else {
                            Image(
                                modifier = Modifier
                                    .size(60.dp)
                                    .zIndex(1f)
                                    .fillMaxHeight(0.14f)
                                    .fillMaxWidth(0.24f)
                                    .absolutePadding(0.dp, 15.dp, 0.dp, 0.dp),

                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(id = R.drawable.logo_light),
                                contentDescription = "Logo Light",
                            ) // Full bilde
                        }
                    }
                    Box(
                        modifier = Modifier.width(70.dp)
                    ) {
                        IconButton(onClick = {}) {
                            if (adrok == "true") {
                                Icon(
                                    modifier = Modifier.size(25.dp),
                                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_where_to_vote_24),
                                    contentDescription = "Lokasjon god",
                                    tint = Color.Green
                                    //Endre farge ved true false po juridiske omroder
                                )
                            } else {
                                Icon(
                                    modifier = Modifier.size(25.dp),
                                    imageVector = ImageVector.vectorResource(id = R.drawable.sharp_wrong_location_24),
                                    contentDescription = "Feil lokasjon",
                                    tint = Color.Red
                                    //Endre farge ved true false po juridiske omroder
                                )
                            }
                        }


                    }

                } else {
                    Image(
                        modifier = Modifier
                            .size(5.dp)
                            .zIndex(1f)
                            .fillMaxHeight(0.14f)
                            .fillMaxWidth(0.24f)
                            .absolutePadding(0.dp, 15.dp, 0.dp, 0.dp),

                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.logo_dark),
                        contentDescription = "",
                    ) // Mindre bilde
                }
            }
            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.025f))

            //Logo

            //Spacer Hvit linje
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.001f)
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
            )

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))

            //Rad for nedbor: 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .height(85.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {

                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.sharp_rainy_24),
                    contentDescription = "Rain icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight()
                        .fillMaxWidth(0.27f)

                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Nedbør",
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center


                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )

                if (checkConnectivityStatus()) {
                    if (nedbor == null || isRefreshing) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                    } else {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .wrapContentHeight()
                                .fillMaxWidth(0.22f)

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                Spacer(
                                    modifier = Modifier.width(width = 8.5.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "1.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$nedbor",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_hoyre),
                                        contentDescription = "Pil hoyre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "2.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$nedbor2",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_midt),
                                        contentDescription = "Pil midt",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "3.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$nedbor3",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_venstre),
                                        contentDescription = "Pil venstre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 7.dp)
                                )
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                }


                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )
                if (checkConnectivityStatus()) {

                    if (nedborGood == true) {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.is_good),
                            contentDescription = "",
                            tint = Color.Green
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.sharp_close_small_24),
                            contentDescription = "",
                            tint = Color.Red
                        )

                    }
                } else {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_wifi_off),
                        contentDescription = "",
                        tint = Color.Yellow
                    )

                }
            }

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))

            //Spacer Hvit Linje
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.001f)
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally)
            )

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))

            //Rad for siktSkydekke: 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .height(85.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.sharp_cloud_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight()
                        .fillMaxWidth(0.27f)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Skydekke",
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )

                if (checkConnectivityStatus()) {
                    if (siktSkydekke == null || isRefreshing) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                    } else {

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .wrapContentHeight()
                                .fillMaxWidth(0.22f)

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                Spacer(
                                    modifier = Modifier.width(width = 8.5.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "1.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$siktSkydekke",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_hoyre),
                                        contentDescription = "Pil hoyre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "2.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$siktSkydekke2",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_midt),
                                        contentDescription = "Pil midt",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "3.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$siktSkydekke3",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_venstre),
                                        contentDescription = "Pil venstre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 7.dp)
                                )
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                }

                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )
                if (checkConnectivityStatus()) {
                    if (skyGood == true) {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.is_good),
                            contentDescription = "",
                            tint = Color.Green
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.sharp_close_small_24),
                            contentDescription = "",
                            tint = Color.Red
                        )

                    }
                } else {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_wifi_off),
                        contentDescription = "",
                        tint = Color.Yellow
                    )

                }
            }

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))

            //Spacer Hvit Linje
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.001f)
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally)
            )

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))


            //rad for vind
            Column(modifier = Modifier

                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { isExpanded = !isExpanded }
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .height(85.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.sharp_wind_power_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(
                        modifier = Modifier
                            .height(7.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .width(1.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .wrapContentHeight()
                            .fillMaxWidth(0.27f)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Vind",
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(7.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .width(1.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .wrapContentHeight()
                            .fillMaxWidth(0.22f)

                    ) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center),
                            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Minimise" else "Expand",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(7.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .width(1.dp)
                    )

                    if (checkConnectivityStatus()) {
                        if (vindUnder10Good == true && vindOver10Good == true) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.is_good),
                                contentDescription = "",
                                tint = Color.Green
                            )
                        } else {

                            Icon(
                                modifier = Modifier.size(50.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.sharp_close_small_24),
                                contentDescription = "",
                                tint = Color.Red
                            )

                        }
                    } else {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_wifi_off),
                            contentDescription = "",
                            tint = Color.Yellow
                        )

                    }
                }
                AnimatedVisibility(visible = isExpanded) {
                    if (vindOver10 != null) {
                        if (vindUnder10 != null) {
                            if (vindUnder10Good != null) {

                                VindDataListe(vindOver10, vindUnder10, vindUnder10Good)

                            }
                        }
                    }


                }
            }


            //Spacer Hvit Linje
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.001f)
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally)
            )

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))


            //Rad for siktToke: 3
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .height(85.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.sharp_foggy_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight()
                        .fillMaxWidth(0.27f)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Tåke",
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )

                if (checkConnectivityStatus()) {
                    if (siktToke == null || isRefreshing) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                    } else {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .wrapContentHeight()
                                .fillMaxWidth(0.22f)

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                Spacer(
                                    modifier = Modifier.width(width = 8.5.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "1.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$siktToke",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_hoyre),
                                        contentDescription = "Pil hoyre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "2.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$siktToke2",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_midt),
                                        contentDescription = "Pil midt",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "3.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$siktToke3",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_venstre),
                                        contentDescription = "Pil venstre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 7.dp)
                                )
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                }

                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )
                if (checkConnectivityStatus()) {
                    if (tokeGood == true) {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.is_good),
                            contentDescription = "",
                            tint = Color.Green
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.sharp_close_small_24),
                            contentDescription = "",
                            tint = Color.Red
                        )

                    }
                } else {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_wifi_off),
                        contentDescription = "",
                        tint = Color.Yellow
                    )

                }
            }

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))

            //Spacer Hvit Linje
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.001f)
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally)
            )

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))

            //Rad for DewPoint: 4
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .height(85.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.sharp_dew_point_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight()
                        .fillMaxWidth(0.27f)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "DewPoint",
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center

                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )

                if (checkConnectivityStatus()) {
                    if (dewPoint == null || isRefreshing) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                    } else {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .wrapContentHeight()
                                .fillMaxWidth(0.22f)

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                Spacer(
                                    modifier = Modifier.width(width = 8.5.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "1.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$dewPoint",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_hoyre),
                                        contentDescription = "Pil hoyre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "2.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$dewPoint2",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_midt),
                                        contentDescription = "Pil midt",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "3.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$dewPoint3",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_venstre),
                                        contentDescription = "Pil venstre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 7.dp)
                                )
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                }

                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )
                if (checkConnectivityStatus()) {
                    if (dewGood == true) {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.is_good),
                            contentDescription = "",
                            tint = Color.Green
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.sharp_close_small_24),
                            contentDescription = "",
                            tint = Color.Red
                        )

                    }
                } else {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_wifi_off),
                        contentDescription = "",
                        tint = Color.Yellow
                    )

                }
            }

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))

            //Spacer Hvit Linje
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.001f)
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally)
            )

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))

            //Rad for RelativeHumidity: 5
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .height(85.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.sharp_humidity_percentage_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight()
                        .fillMaxWidth(0.27f)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Relative Humidity",
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center


                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )

                if (checkConnectivityStatus()) {
                    if (relativeHumidity == null || isRefreshing) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                    } else {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .wrapContentHeight()
                                .fillMaxWidth(0.22f)

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                Spacer(
                                    modifier = Modifier.width(width = 8.5.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "1.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$relativeHumidity",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_hoyre),
                                        contentDescription = "Pil hoyre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "2.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$relativeHumidity2",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_midt),
                                        contentDescription = "Pil midt",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 20.dp)
                                )
                                Column(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "3.Time",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 8.1.sp
                                    )
                                    Text(
                                        text = "$relativeHumidity3",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 18.sp
                                    )
                                    Icon(
                                        modifier = Modifier.size(15.dp),
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_pil_venstre),
                                        contentDescription = "Pil venstre",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.width(width = 7.dp)
                                )
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                }

                Spacer(
                    modifier = Modifier
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .width(1.dp)
                )
                if (checkConnectivityStatus()) {
                    if (humGood == true) {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.is_good),
                            contentDescription = "",
                            tint = Color.Green
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.sharp_close_small_24),
                            contentDescription = "",
                            tint = Color.Red
                        )

                    }
                } else {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_wifi_off),
                        contentDescription = "",
                        tint = Color.Yellow
                    )

                }
            }

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))

            //Spacer Hvit Linje
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.001f)
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally)
            )

            //Spacer Linje til text
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))


        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun VindDataListe(
    vindOver10: VindOver10mDataklasse,
    vindUnder10: MutableList<MutableList<String>>,
    vindUnder10Good: Boolean
) {

    Column {
        LazyColumn(
            modifier = Modifier
                .height(300.dp)
                .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            vindOver10.vindData.entries.forEach { entry ->
                item {
                    VindDataItem(entry)
                }
            }
            item {
                vindUnder10mDataItem(vindUnder10, vindUnder10Good)
            }
        }

    }
}


@Composable
fun VindDataItem(entry: Map.Entry<String, DataTrykkFlate>) {
    Row(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val moh = (round(entry.value.meterOverHavet / 200) * 200).toInt()
        val styrke = round(entry.value.styrke).toInt()
        val shear = round(entry.value.shearvind).toInt()
        val shearInnenMax = entry.value.shearInnenforMax
        val vindInnenMax = entry.value.vindInnenforMax
        val darkGreen = Color(0xFF006600)

        val annotatedString = buildAnnotatedString {

            append("$moh moh - ")


            withStyle(style = SpanStyle(color = if (vindInnenMax) darkGreen else Color.Red)) {
                append("$styrke m/s ")
            }


            append("- Shear: ")


            withStyle(style = SpanStyle(color = if (shearInnenMax) darkGreen else Color.Red)) {
                append("$shear m/s")
            }
        }

        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

}


@Composable
fun vindUnder10mDataItem(vindUnder10: MutableList<MutableList<String>>, vindUnder10Good: Boolean) {
    Row(
        modifier = Modifier
            .padding(2.dp)
            .wrapContentWidth()
    ) {
        val styrke = vindUnder10[0][1].toDouble()
        Log.d("VindUnder10 mutable string ", vindUnder10.toString())
        val vindInnenMax = vindUnder10Good
        val darkGreen = Color(0xFF006600)

        val annotatedString = buildAnnotatedString {

            append("Bakkenivå -")

            withStyle(style = SpanStyle(color = if (vindInnenMax == true) darkGreen else Color.Red)) {
                append("$styrke m/s ")
            }

        }
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


