package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

// Для второй отдельной сборки задания 1 замените "Амир" на null.
private val greetingName: String? = "Амир"
private const val INITIALS = "А"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MyApplicationTheme { PracticeApp(name = greetingName) } }
    }
}

@Composable
fun PracticeApp(name: String?) {
    var task by rememberSaveable { mutableIntStateOf(1) }
    val taskState = rememberSaveableStateHolder()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier.fillMaxSize().padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Модуль 2 · Задания 1–10", style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp))
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..10).forEach { number ->
                    FilterChip(selected = task == number, onClick = { task = number },
                        label = { Text("Задание $number") })
                }
            }
            Spacer(Modifier.height(16.dp))
            taskState.SaveableStateProvider(task) {
              when (task) {
                1 -> Column(Modifier.padding(16.dp)) {
                    Hello(name)
                    Spacer(Modifier.height(16.dp))
                    Text("Проверка второго случая:", style = MaterialTheme.typography.labelLarge)
                    Hello(null)
                }
                2 -> Column(Modifier.padding(16.dp)) {
                    Hello(name)
                    Spacer(Modifier.height(16.dp))
                    Text("Три макета portrait, landscape и round находятся у функции HelloPreview. Откройте режим Split или Design в Android Studio.")
                }
                3 -> TextExamples(Modifier.padding(horizontal = 16.dp))
                4 -> TaskButton()
                5 -> InitialsContainer(INITIALS)
                6 -> CircleExamples()
                7 -> ContactExamples()
                8 -> WaterTracker()
                9 -> Column(Modifier.padding(16.dp)) {
                    Text("UI-тесты трекера воды", style = MaterialTheme.typography.titleMedium)
                    Text("Исходный код тестов находится в WaterTrackerTest.kt в папке androidTest. Запустите этот класс в Android Studio.")
                }
                10 -> ShoppingCartScreen()
              }
            }
        }
    }
}

// Задание 1. Nullable-параметр и приветствие.
@Composable
fun Hello(name: String?) {
    Text(text = if (name == null) "Имя не задано" else "Привет, $name!")
}

// Задание 2. Три конфигурации одной функции предпросмотра.
@Preview(name = "portrait", group = "Задание 2", showSystemUi = true,
    device = "spec:width=360dp,height=640dp,dpi=160")
@Preview(name = "landscape", group = "Задание 2", showSystemUi = true,
    device = "spec:width=640dp,height=360dp,dpi=160")
@Preview(name = "round", group = "Задание 2", showSystemUi = true,
    device = "spec:width=200dp,height=200dp,dpi=160,isRound=true",
    widthDp = 200, heightDp = 200, showBackground = true, backgroundColor = 0xFFFFFF00)
@Composable
fun HelloPreview() {
    MyApplicationTheme {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Hello("Амир") }
    }
}

@Preview(name = "null", group = "Задание 1", showBackground = true)
@Composable
fun HelloNullPreview() { MyApplicationTheme { Hello(null) } }

// Задание 3. Общая строка читается из ресурсов strings.xml.
@Composable
fun GreenItalicText(modifier: Modifier = Modifier) {
    Text(text = stringResource(R.string.compose_description),
        modifier = modifier.fillMaxWidth(), color = Color.Green, fontSize = 16.sp,
        fontStyle = FontStyle.Italic, textAlign = TextAlign.Center)
}

@Composable
fun EllipsisText(modifier: Modifier = Modifier) {
    Text(text = stringResource(R.string.compose_description),
        modifier = modifier.fillMaxWidth(), maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Composable
fun IndentedText(modifier: Modifier = Modifier) {
    Text(text = stringResource(R.string.compose_description),
        modifier = modifier.fillMaxWidth().background(Color.Green),
        color = Color.Black, fontSize = 24.sp, textDecoration = TextDecoration.Underline,
        // Отступ первой строки измеряется в sp, как указано в задании.
        style = TextStyle(textIndent = TextIndent(firstLine = 48.sp)))
}

@Composable
fun TextExamples(modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        GreenItalicText()
        EllipsisText()
        IndentedText()
    }
}

@Preview(name = "Зелёный курсив", group = "Задание 3", widthDp = 360, showBackground = true)
@Composable
fun GreenItalicPreview() { MyApplicationTheme { GreenItalicText() } }

@Preview(name = "Одна строка", group = "Задание 3", widthDp = 360, showBackground = true)
@Composable
fun EllipsisPreview() { MyApplicationTheme { EllipsisText() } }

@Preview(name = "Отступ первой строки", group = "Задание 3", widthDp = 360, showBackground = true)
@Composable
fun IndentedPreview() { MyApplicationTheme { IndentedText() } }

// Задание 4. Кнопка по образцу из методички.
@Composable
fun TaskButton(modifier: Modifier = Modifier) {
    var clicks by rememberSaveable { mutableIntStateOf(0) }
    Column(modifier.fillMaxWidth()) {
        Button(onClick = { clicks++ }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp), border = BorderStroke(2.dp, Color.Gray),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray,
                contentColor = Color.Black)) {
            Text("Нажми на меня")
        }
        if (clicks > 0) Text("Нажатий: $clicks", modifier = Modifier.padding(16.dp))
    }
}

@Preview(name = "Кнопка", group = "Задание 4", widthDp = 360, showBackground = true)
@Composable
fun ButtonPreview() { MyApplicationTheme { TaskButton() } }

// Задание 5. 100 dp − 16 dp сверху − 16 dp снизу = диаметр 68 dp.
@Composable
fun InitialsContainer(initials: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().height(100.dp).background(Color.LightGray)
        .padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.fillMaxHeight().aspectRatio(1f).clip(CircleShape)
            .background(Color(0xFFFF5722)), contentAlignment = Alignment.Center) {
            Text(text = initials, color = Color.Black)
        }
    }
}

@Preview(name = "Инициалы", group = "Задание 5", widthDp = 360, showBackground = true)
@Composable
fun InitialsPreview() { MyApplicationTheme { InitialsContainer(INITIALS) } }

