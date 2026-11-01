package com.example.myapplication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.random.Random

// Задание 6. Ресурс сохраняет исходный размер 48 × 48 dp.
@Composable
fun OriginalCircle(modifier: Modifier = Modifier) {
    Box(modifier.size(240.dp, 120.dp).background(Color.Black), contentAlignment = Alignment.TopEnd) {
        Image(painterResource(R.drawable.circle), "Оранжевый круг",
            modifier = Modifier.size(48.dp))
    }
}

@Composable
fun StretchedCircle(modifier: Modifier = Modifier) {
    Box(modifier.size(240.dp, 120.dp).background(Color.Blue), contentAlignment = Alignment.Center) {
        Image(painterResource(R.drawable.circle), "Фиолетовый растянутый круг",
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(Color(0xFF800080)))
    }
}

@Composable
fun CircleExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OriginalCircle()
        StretchedCircle()
    }
}

@Preview(name = "Исходный круг", group = "Задание 6", showBackground = true)
@Composable
fun OriginalCirclePreview() { OriginalCircle() }

@Preview(name = "Растянутый круг", group = "Задание 6", showBackground = true)
@Composable
fun StretchedCirclePreview() { StretchedCircle() }

data class Contact(val firstName: String, val patronymic: String, val lastName: String,
    val phone: String?, val address: String)

private val contacts = listOf(
    Contact("Евгений", "Андреевич", "Лукашин", "+7 495 95 95 95",
        "г. Москва, 3-я улица Строителей, д. 25, кв. 12"),
    Contact("Василий", "Егорович", "Кузякин", null, "Ивановская область, дер. Крутово, д. 4"),
    Contact("Людмила", "Прокофьевна", "Калугина", "+7 495 788 78 78",
        "Москва, Большая Никитская, д. 43, кв. 290")
)

// Задание 7. Один контакт, список контактов и строка с изображением справа.
@Composable
fun ContactColumn(contact: Contact, modifier: Modifier = Modifier, centered: Boolean = false) {
    Column(modifier, horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start) {
        val align = if (centered) TextAlign.Center else TextAlign.Start
        Text("Имя: ${contact.firstName}", textAlign = align)
        Text("Отчество: ${contact.patronymic}", textAlign = align)
        Text("Фамилия: ${contact.lastName}", textAlign = align)
        Text("Мобильный телефон: ${contact.phone ?: "---"}", textAlign = align)
        Text("Адрес: ${contact.address}", textAlign = align)
    }
}

@Composable
fun ContactList(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        contacts.forEach { contact ->
            ContactColumn(contact, Modifier.fillMaxWidth().background(Color.LightGray).padding(8.dp), centered = true)
        }
    }
}

@Composable
fun ContactRow(modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().padding(8.dp).background(Color.LightGray).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        ContactColumn(contacts.first(), Modifier.weight(1f), centered = true)
        Image(painterResource(R.drawable.contact_star), "Избранный контакт",
            Modifier.padding(start = 8.dp).size(24.dp))
    }
}

@Composable
fun ContactExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ContactColumn(contacts.first())
        ContactList()
        ContactRow()
    }
}

@Preview(name = "Контакт Column", group = "Задание 7", widthDp = 400, showBackground = true)
@Composable
fun ContactColumnPreview() { MyApplicationTheme { ContactColumn(contacts.first()) } }

@Preview(name = "Список Column", group = "Задание 7", widthDp = 400, showBackground = true)
@Composable
fun ContactListPreview() { MyApplicationTheme { ContactList() } }

@Preview(name = "Контакт Row", group = "Задание 7", widthDp = 400, showBackground = true)
@Composable
fun ContactRowPreview() { MyApplicationTheme { ContactRow() } }

// Задание 8. Сохраняем исходное начальное значение 100 мл из условия.
@Composable
fun WaterTracker(modifier: Modifier = Modifier) {
    var waterCount by rememberSaveable { mutableIntStateOf(100) }
    var consecutiveDays by rememberSaveable { mutableIntStateOf(0) }
    Column(modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Трекер воды", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF415F91))
        Spacer(Modifier.height(48.dp))
        Text("$waterCount мл", Modifier.testTag("water_amount"), fontSize = 48.sp,
            fontWeight = FontWeight.Bold, color = Color(0xFF565F71))
        Spacer(Modifier.height(24.dp))
        Button(onClick = { waterCount += 250 },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF415F91), contentColor = Color.White)) {
            Text("+250 мл", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = {
            consecutiveDays = if (waterCount >= 1500) consecutiveDays + 1 else 0
            waterCount = 0
        }) { Text("Завершить день") }
        Spacer(Modifier.height(24.dp))
        Text("Дней подряд с 1500 мл и больше: $consecutiveDays",
            Modifier.testTag("water_streak"), textAlign = TextAlign.Center)
    }
}

@Preview(name = "Трекер воды", group = "Задание 8", widthDp = 360, showBackground = true)
@Composable
fun WaterTrackerPreview() { MyApplicationTheme { WaterTracker() } }

data class Product(val id: Int, val name: String, val price: Int)
data class Cart(val products: List<Product>)

private val initialProducts = listOf(
    Product(0, "Товар #1", 100), Product(1, "Товар #2", 150), Product(2, "Товар #3", 56)
)

private val cartSaver = listSaver<Cart, Int>(
    save = { cart -> cart.products.flatMap { listOf(it.id, it.price) } },
    restore = { values -> Cart(values.chunked(2).map { Product(it[0], "Товар #${it[0] + 1}", it[1]) }) }
)

// Задание 10. Сумма каждый раз вычисляется из списка, а Toast вызывается только событием добавления.
@Composable
fun ShoppingCartScreen(modifier: Modifier = Modifier, nextPrice: () -> Int = { Random.nextInt(0, 100) }) {
    var cart by rememberSaveable(stateSaver = cartSaver) { mutableStateOf(Cart(initialProducts)) }
    var nextId by rememberSaveable { mutableIntStateOf(3) }
    val context = LocalContext.current
    val totalSum = cart.products.sumOf { it.price }
    Column(modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Корзина магазина", style = MaterialTheme.typography.headlineSmall)
        cart.products.forEach { product -> Text("${product.name} - ${product.price} рублей") }
        Text("Товаров на сумму: $totalSum рублей", Modifier.testTag("cart_total"))
        AddProductSection {
            val updated = cart.products + Product(nextId, "Товар #${nextId + 1}", nextPrice())
            nextId++
            cart = Cart(updated)
            if (updated.sumOf { it.price } > 500) {
                Toast.makeText(context, "Доставка бесплатная!", Toast.LENGTH_SHORT).show()
            }
        }
        RemoveProductSection(cart.products.isNotEmpty()) { cart = Cart(cart.products.dropLast(1)) }
    }
}

@Composable
fun AddProductSection(onAdd: () -> Unit) { Button(onClick = onAdd) { Text("Добавить товар") } }

@Composable
fun RemoveProductSection(hasProducts: Boolean, onRemove: () -> Unit) {
    if (hasProducts) Button(onClick = onRemove) { Text("Удалить товар") }
}

@Preview(name = "Корзина", group = "Задание 10", widthDp = 360, showBackground = true)
@Composable
fun ShoppingCartPreview() { MyApplicationTheme { ShoppingCartScreen() } }
