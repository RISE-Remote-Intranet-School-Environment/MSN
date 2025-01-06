package org.schoolapp.project


val courseFiles = mapOf(
    "Data Science" to listOf("Intro.pdf", "ML.pptx", "Python.docx"),
    "History" to listOf("Ancient.pdf", "WWII.pptx", "Modern.docx"),
    "Science" to listOf("Physics.pdf", "Biology.pptx", "Chemistry.docx")
)

val courseDetails = mapOf(
    "Data Science" to """
        This course provides an extensive overview of data analysis, machine learning, and visualization techniques. 
        You will learn to work with tools such as Python, R, and SQL to manipulate and analyze large datasets.
        The course also includes topics like data wrangling, predictive modeling, and statistical inference.
        By the end, you will complete a capstone project to apply these concepts in real-world scenarios.
    """.trimIndent(),
    "History" to """
        Dive into the fascinating world of history with this comprehensive course. 
        Explore key historical events, such as the Industrial Revolution, the World Wars, and the rise and fall of civilizations. 
        We also focus on historical methodology, helping you understand how historians analyze sources and construct narratives.
        This course includes group discussions, document analysis, and essays to refine your critical thinking skills.
    """.trimIndent(),
    "Science" to """
        This foundational course explores core concepts in physics, chemistry, and biology. 
        You will conduct experiments to understand laws like Newton's mechanics, chemical reactions, and the process of photosynthesis.
        Additionally, we cover contemporary topics like climate change, genetic engineering, and renewable energy.
        The course encourages hands-on learning and includes lab sessions to deepen your understanding.
    """.trimIndent()
)
