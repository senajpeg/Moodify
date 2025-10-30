package com.senaaksoy.moodify.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.components.Mood
import com.senaaksoy.moodify.components.MoodCard
import com.senaaksoy.moodify.navigation.Screen
import com.senaaksoy.moodify.viewmodel.DeezerViewModel

@Composable
fun PickMoodScreen(
    deezerviewModel: DeezerViewModel = hiltViewModel(),
    navController: NavController
) {
    val selectedMood by deezerviewModel.selectedMood.collectAsState()

    val moods = listOf(
        Mood(stringResource(R.string.happy), R.drawable.happy),
        Mood(stringResource(R.string.sad), R.drawable.sad),
        Mood(stringResource(R.string.energetic), R.drawable.energy),
        Mood(stringResource(R.string.chill), R.drawable.chill)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A0B2E),
                        Color(0xFF2D1B55)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.09f))

        Text(
            text = stringResource(R.string.how_are_you_feeling),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE37EF3),
                        Color(0xFF68B3F6)
                    )
                )
            )
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.09f))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp)
        ) {
            items(moods) { mood ->
                MoodCard(
                    mood = mood,
                    isSelected = selectedMood == mood.name,
                    onMoodClick = { deezerviewModel.selectMood(mood.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                selectedMood?.let { mood ->
                    navController.navigate(Screen.createShowPlaylistRoute(mood))
                }
            },
            enabled = selectedMood != null,
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .widthIn(max = 300.dp)
                .padding(bottom = 24.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFA065E3),
                            Color(0xFF5E8BCB)
                        )
                    )
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            )
        ) {
            Text(text = stringResource(R.string.generate))
        }
    }
}

@Preview
@Composable
fun PickMoodPreview() {
    val navController = rememberNavController()
    PickMoodScreen(navController = navController)
}