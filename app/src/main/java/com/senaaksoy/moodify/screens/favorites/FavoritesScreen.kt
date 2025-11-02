package com.senaaksoy.moodify.screens.favorites


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.components.FavoriteTrackCard
import com.senaaksoy.moodify.model.toDeezerTrack
import com.senaaksoy.moodify.navigation.Screen
import com.senaaksoy.moodify.viewmodel.DeezerViewModel

@Composable
fun FavouritesScreen(
    navController: NavController,
    deezerViewModel: DeezerViewModel = hiltViewModel()
) {
    val favorites = deezerViewModel.favorites.collectAsState().value
    val currentPlayingTrackId = deezerViewModel.currentPlayingTrackId.collectAsState().value
    val favoriteTrackIds = deezerViewModel.favoriteTrackIds.collectAsState().value

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
            )
    ) {
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFB685F1),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.no_favorites_yet),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.add_songs_to_your_favorites),
                        fontSize = 14.sp,
                        color = Color(0xFFB685F1),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.PickMoodScreen.route)
                        }
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)
            ) {
                items(favorites) { favoriteTrack ->
                    val track = favoriteTrack.toDeezerTrack()
                    FavoriteTrackCard(
                        track = track,
                        isPlaying = currentPlayingTrackId == track.id,
                        isFavorite = favoriteTrackIds.contains(track.id),
                        onPlayPauseClick = {
                            deezerViewModel.playPauseTrack(track)
                        },
                        onFavoriteClick = {
                            deezerViewModel.toggleFavorite(track)
                        }
                    )
                }
            }
        }
    }
}
