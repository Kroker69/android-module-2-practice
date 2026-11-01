package com.example.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test

// Задание 9. UI-тесты настоящих кнопок и отображаемых значений.
class WaterTrackerTest {
    @get:Rule val compose = createComposeRule()
    private fun start() { compose.setContent { MyApplicationTheme { WaterTracker() } } }
    private fun add(times: Int) { repeat(times) { compose.onNodeWithText("+250 мл").performClick() } }
    private fun endDay() { compose.onNodeWithText("Завершить день").performClick() }
    private fun amount(value: Int) { compose.onNodeWithTag("water_amount").assertTextEquals("$value мл") }
    private fun streak(value: Int) {
        compose.onNodeWithTag("water_streak").assertTextEquals("Дней подряд с 1500 мл и больше: $value")
    }

    @Test fun addButtonAdds250EachTime() {
        start()
        compose.onNodeWithText("+250 мл").assertIsDisplayed().assertHasClickAction()
        amount(100)
        add(1)
        amount(350)
        add(2)
        amount(850)
    }

    @Test fun finishButtonResetsWater() {
        start()
        compose.onNodeWithText("Завершить день").assertIsDisplayed().assertHasClickAction()
        add(2)
        endDay()
        amount(0)
        streak(0)
    }

    @Test fun initialStreakTextIsShown() { start(); streak(0) }

    @Test fun exactly1500CountsAsSuccessfulDay() {
        start()
        endDay()
        add(6)
        amount(1500)
        endDay()
        amount(0)
        streak(1)
    }

    @Test fun moreThan1500CountsAsSuccessfulDay() {
        start()
        add(6)
        amount(1600)
        endDay()
        streak(1)
    }

    @Test fun consecutiveSuccessfulDaysIncrementStreak() {
        start()
        endDay()
        repeat(2) { add(6); endDay() }
        amount(0)
        streak(2)
    }

    @Test fun insufficientWaterBreaksStreak() {
        start()
        add(6)
        endDay()
        streak(1)
        add(5)
        amount(1250)
        endDay()
        streak(0)
        amount(0)
    }

    @Test fun emptyDayBreaksStreak() {
        start()
        add(6)
        endDay()
        endDay()
        streak(0)
    }

    @Test fun countersSurviveStateRestoration() {
        val restoration = StateRestorationTester(compose)
        restoration.setContent { MyApplicationTheme { WaterTracker() } }
        add(6)
        endDay()
        add(1)
        restoration.emulateSavedInstanceStateRestore()
        amount(250)
        streak(1)
    }
}
