package no.uio.ifi.in2000.thedl.myapplication

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import no.uio.ifi.in2000.thedl.myapplication.ui.navigation.Navigation
import org.junit.Rule
import org.junit.Test

class AndroidTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    val paramater = hasContentDescription("Parameter")

    val home = hasContentDescription("Home")

    val maxRegn = hasContentDescription("maxRegn")

    val maxTåke = hasContentDescription("maxTåke")

    val maxSkydekke = hasContentDescription("maxSkydekke")

    val maxGust = hasContentDescription("maxGust")

    val maxDewPoint = hasContentDescription("maxDewPoint")

    val maxRelativHumidity = hasContentDescription("maxRelativHumidity")

    val maxSheerWind = hasContentDescription("maxSheerWind")

    val maxVindILufta = hasContentDescription("maxVindILufta")

    val saveChanges = hasContentDescription("Save Changes") and hasClickAction()

    val defaultValues = hasContentDescription("Set Default") and hasClickAction()


    @Test
    fun EndreRegnParameterTest() {

        composeTestRule.setContent { Navigation().Navigation() }

        composeTestRule.onNode(paramater).performClick()

        composeTestRule.onNode(maxRegn).performTextReplacement("15.0")

        composeTestRule.onNode(saveChanges).performClick()

        composeTestRule.onNode(maxRegn).assertTextContains("15.0")


    }

    @Test
    fun EndreTåkeParameterTest() {

        composeTestRule.setContent { Navigation().Navigation() }

        composeTestRule.onNode(paramater).performClick()

        composeTestRule.onNode(maxTåke).performTextReplacement("30.0")

        composeTestRule.onNode(saveChanges).performClick()

        composeTestRule.onNode(maxTåke).assertTextContains("30.0")


    }

    @Test
    fun EndreSkydekkeParameterTest() {

        composeTestRule.setContent { Navigation().Navigation() }

        composeTestRule.onNode(paramater).performClick()

        composeTestRule.onNode(maxSkydekke).performTextReplacement("1.0")

        composeTestRule.onNode(saveChanges).performClick()

        composeTestRule.onNode(maxSkydekke).assertTextContains("1.0")

    }

    @Test
    fun EndreGustParameterTest() {

        composeTestRule.setContent { Navigation().Navigation() }

        composeTestRule.onNode(paramater).performClick()

        composeTestRule.onNode(maxGust).performTextReplacement("9.9")

        composeTestRule.onNode(saveChanges).performClick()

        composeTestRule.onNode(maxGust).assertTextContains("9.9")


    }

    @Test
    fun EndreDewPointParameterTest() {

        composeTestRule.setContent { Navigation().Navigation() }

        composeTestRule.onNode(paramater).performClick()

        composeTestRule.onNode(maxDewPoint).performTextReplacement("10")

        composeTestRule.onNode(saveChanges).performClick()

        composeTestRule.onNode(maxDewPoint).assertTextContains("10")


    }

    @Test
    fun EndreRelativHumidityParameterTest() {

        composeTestRule.setContent { Navigation().Navigation() }

        composeTestRule.onNode(paramater).performClick()

        composeTestRule.onNode(maxRelativHumidity).performTextReplacement("95.0")

        composeTestRule.onNode(saveChanges).performClick()

        composeTestRule.onNode(maxRelativHumidity).assertTextContains("95.0")


    }

    @Test
    fun EndreMaxSheerWindParameterTest() {

        composeTestRule.setContent { Navigation().Navigation() }

        composeTestRule.onNode(paramater).performClick()

        composeTestRule.onNode(maxSheerWind).performTextReplacement("27.6")

        composeTestRule.onNode(saveChanges).performClick()

        composeTestRule.onNode(maxSheerWind).assertTextContains("27.6")


    }

    @Test
    fun EndreMaxVindILuftaParameterTest() {

        composeTestRule.setContent { Navigation().Navigation() }

        composeTestRule.onNode(paramater).performClick()

        composeTestRule.onNode(maxVindILufta).performTextReplacement("28.9")

        composeTestRule.onNode(saveChanges).performClick()

        composeTestRule.onNode(maxVindILufta).assertTextContains("28.9")


    }

    @Test
    fun DefaultValueTest() {

        composeTestRule.setContent { Navigation().Navigation() }

        composeTestRule.onNode(paramater).performClick()


        composeTestRule.onNode(maxGust).performTextReplacement("9.9")


        composeTestRule.onNode(maxSkydekke).performTextReplacement("0.30")


        composeTestRule.onNode(maxTåke).performTextReplacement("30.0")


        composeTestRule.onNode(maxRegn).performTextReplacement("15.0")


        composeTestRule.onNode(saveChanges).performClick()


        composeTestRule.onNode(defaultValues).performClick()


        composeTestRule.onNode(maxGust).assertTextContains("8.6")


        composeTestRule.onNode(maxSkydekke).assertTextContains("15.0")


        composeTestRule.onNode(maxTåke).assertTextContains("0.0")


        composeTestRule.onNode(maxRegn).assertTextContains("0.0")

    }



}