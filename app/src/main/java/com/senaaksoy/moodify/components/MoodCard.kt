package com.senaaksoy.moodify.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Mood(
    val name: String,
    val iconRes: Int
)

@Composable
fun MoodCard(
    mood: Mood,
    isSelected: Boolean = false,
    onMoodClick: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
    ) {
        val cardHeight = maxHeight
        Card(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 3.dp,
                            color = Color(0xFFE37EF3),
                            shape = RoundedCornerShape(16.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .clickable { onMoodClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) Color(0xFF3F3570) else Color(0xFF2F285A)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(cardHeight * 0.15f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = mood.iconRes),
                    contentDescription = mood.name,
                    tint = if (isSelected) Color(0xFFE37EF3) else Color(0xFFB685F1),
                    modifier = Modifier.size(cardHeight * 0.33f)
                )
                Spacer(modifier = Modifier.height(cardHeight * 0.08f))
                Text(
                    text = mood.name,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (cardHeight.value * 0.15f).sp
                )
            }
        }
    }
}