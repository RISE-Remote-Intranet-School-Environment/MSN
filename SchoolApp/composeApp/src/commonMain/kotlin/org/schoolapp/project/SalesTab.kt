package org.schoolapp.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class SaleItem(
    val title: String,
    val imageUrl: String,
    val price: String
)

@Composable
fun SalesTab() {
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    if (selectedCategory == null) {
        // Category selection screen
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { selectedCategory = "Books & Syllabi" },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Books & Syllabi")
            }
            Button(
                onClick = { selectedCategory = "Lab & Other Equipment" },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Lab & Other Equipment")
            }
            Button(
                onClick = { selectedCategory = "Student Housing Furniture" },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Student Housing Furniture")
            }
        }
    } else {
        // Show items for the selected category
        CategoryItemsScreen(category = selectedCategory ?: "", onBack = { selectedCategory = null })
    }
}

@Composable
fun CategoryItemsScreen(category: String, onBack: () -> Unit) {
    val itemsForSale = remember { getItemsForCategory(category) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Back Button
        Button(onClick = onBack, modifier = Modifier.padding(bottom = 16.dp)) {
            Text("Back")
        }

        // List of items
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(itemsForSale) { item ->
                SaleItemRow(item = item)
            }
        }

        // Sell Button
        Button(
            onClick = { /* Navigate to item selling form */ },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Sell your stuff")
        }
    }
}

@Composable
fun SaleItemRow(item: SaleItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically // Corrected alignment
    ) {
        // Placeholder for image (Replace with actual image loading logic)
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colors.primary.copy(alpha = 0.2f))
        ) {
            Text(
                text = "Image",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.onSurface
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = item.title, style = MaterialTheme.typography.subtitle1)
            Text(text = item.price, style = MaterialTheme.typography.body2, color = Color.Gray)
        }
    }
    Divider()
}

fun getItemsForCategory(category: String): List<SaleItem> {
    return when (category) {
        "Books & Syllabi" -> listOf(
            SaleItem("Calculus Book", "https://example.com/image1.jpg", "$20"),
            SaleItem("Physics Syllabus", "https://example.com/image2.jpg", "$10")
        )
        "Lab & Other Equipment" -> listOf(
            SaleItem("Microscope", "https://example.com/image3.jpg", "$150"),
            SaleItem("Safety Goggles", "https://example.com/image4.jpg", "$5")
        )
        "Student Housing Furniture" -> listOf(
            SaleItem("Desk Lamp", "https://example.com/image5.jpg", "$15"),
            SaleItem("Bookshelf", "https://example.com/image6.jpg", "$30")
        )
        else -> emptyList()
    }
}
