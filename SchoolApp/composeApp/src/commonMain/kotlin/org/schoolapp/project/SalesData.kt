package org.schoolapp.project

data class SaleItem(
    val title: String,
    val imageUrl: String,
    val price: String
)

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