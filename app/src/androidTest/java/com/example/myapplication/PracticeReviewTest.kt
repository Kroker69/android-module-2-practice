package com.example.myapplication

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.io.File
import org.junit.Rule
import org.junit.Test

class PracticeReviewTest {
    @get:Rule val compose = createComposeRule()
    private fun startTask(number: Int) {
        compose.setContent { MyApplicationTheme { PracticeApp("Амир") } }
        compose.onNodeWithText("Задание $number").performScrollTo().performClick()
    }
    private fun capture(name: String) {
        compose.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val directory = File(instrumentation.targetContext.getExternalFilesDir(null), "review")
        directory.mkdirs()
        val bitmap = instrumentation.uiAutomation.takeScreenshot()
        File(directory, "$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }

    @Test fun greetingHandlesBothCases() {
        compose.setContent { MyApplicationTheme { PracticeApp("Амир") } }
        compose.onNodeWithText("Привет, Амир!").assertIsDisplayed()
        compose.onNodeWithText("Имя не задано").assertIsDisplayed()
        capture("task1")
    }

    @Test fun previewGreetingRenders() {
        compose.setContent { HelloPreview() }
        compose.onNodeWithText("Привет, Амир!").assertIsDisplayed()
        capture("task2-content")
    }

    @Test fun textVariantsRender() {
        startTask(3)
        compose.onAllNodesWithText("Jetpack Compose — это современный набор инструментов для создания пользовательского интерфейса (UI) на Android, разработанный Google").assertCountEquals(3)
        capture("task3")
    }

    @Test fun buttonWorks() {
        startTask(4)
        capture("task4")
        compose.onNodeWithText("Нажми на меня").performClick()
        compose.onNodeWithText("Нажатий: 1").assertIsDisplayed()
    }

    @Test fun initialsRender() {
        startTask(5)
        compose.onNodeWithText("А").assertIsDisplayed()
        capture("task5")
    }

    @Test fun circlesRender() {
        startTask(6)
        compose.onNodeWithContentDescription("Оранжевый круг").assertIsDisplayed()
        compose.onNodeWithContentDescription("Фиолетовый растянутый круг").assertIsDisplayed()
        capture("task6")
    }

    @Test fun contactsRender() {
        startTask(7)
        compose.onNodeWithText("Имя: Василий").assertIsDisplayed()
        compose.onNodeWithText("Мобильный телефон: ---").assertIsDisplayed()
        capture("task7-top")
        compose.onNodeWithContentDescription("Избранный контакт").performScrollTo().assertIsDisplayed()
        capture("task7")
    }

    @Test fun waterScreenRenders() {
        startTask(8)
        compose.onNodeWithText("Трекер воды").assertIsDisplayed()
        capture("task8")
    }

    @Test fun cartTotalsFollowProductsAndRemoveButtonDisappears() {
        compose.setContent { MyApplicationTheme { ShoppingCartScreen(nextPrice = { 99 }) } }
        compose.onNodeWithTag("cart_total").assertTextEquals("Товаров на сумму: 306 рублей")
        compose.onNodeWithText("Добавить товар").performClick()
        compose.onNodeWithTag("cart_total").assertTextEquals("Товаров на сумму: 405 рублей")
        compose.onNodeWithText("Добавить товар").performClick()
        compose.onNodeWithTag("cart_total").assertTextEquals("Товаров на сумму: 504 рублей")
        capture("task10")
        compose.onNodeWithText("Удалить товар").performClick()
        compose.onNodeWithText("Товар #5 - 99 рублей").assertDoesNotExist()
        compose.onNodeWithTag("cart_total").assertTextEquals("Товаров на сумму: 405 рублей")
        repeat(4) { compose.onNodeWithText("Удалить товар").performClick() }
        compose.onNodeWithTag("cart_total").assertTextEquals("Товаров на сумму: 0 рублей")
        compose.onNodeWithText("Удалить товар").assertDoesNotExist()
        compose.onNodeWithText("Добавить товар").performClick()
        compose.onNodeWithTag("cart_total").assertTextEquals("Товаров на сумму: 99 рублей")
    }

    @Test fun cartSurvivesRestorationWithoutInflatingTotal() {
        val restoration = StateRestorationTester(compose)
        restoration.setContent { MyApplicationTheme { ShoppingCartScreen(nextPrice = { 99 }) } }
        compose.onNodeWithText("Добавить товар").performClick()
        restoration.emulateSavedInstanceStateRestore()
        compose.onNodeWithTag("cart_total").assertTextEquals("Товаров на сумму: 405 рублей")
        compose.onNodeWithText("Товар #4 - 99 рублей").assertIsDisplayed()
    }

    @Test fun switchingTasksPreservesWaterCounter() {
        compose.setContent { MyApplicationTheme { PracticeApp("Амир") } }
        compose.onNodeWithText("Задание 8").performScrollTo().performClick()
        compose.onNodeWithText("+250 мл").performClick()
        compose.onNodeWithText("Задание 7").performScrollTo().performClick()
        compose.onNodeWithText("Задание 8").performScrollTo().performClick()
        compose.onNodeWithTag("water_amount").assertTextEquals("350 мл")
    }
}
