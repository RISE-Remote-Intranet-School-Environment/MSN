package org.schoolapp.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.ui.graphics.painter.Painter


@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeSection(
                icon = painterResource(id = R.drawable.calendar),
                label = "Calendar",
                onClick = { onNavigate("Calendar") }
            )
            HomeSection(
                icon = painterResource(id = R.drawable.klass),
                label = "Classes",
                onClick = { onNavigate("Classes") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeSection(
                icon = painterResource(id = R.drawable.collaboration),
                label = "Collaboration",
                onClick = { onNavigate("Collaboration") }
            )
            HomeSection(
                icon = painterResource(id = R.drawable.grades),
                label = "Grades",
                onClick = { onNavigate("Grades") }
            )
        }
    }
}

@Composable
fun HomeSection(icon: Painter, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = icon,
            contentDescription = label,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(label)
    }
}
