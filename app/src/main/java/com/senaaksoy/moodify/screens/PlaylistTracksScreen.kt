package com.senaaksoy.moodify.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.senaaksoy.moodify.components.TrackCard
import com.senaaksoy.moodify.navigation.Screen
import com.senaaksoy.moodify.viewmodel.DeezerViewModel

@Composable
fun PlaylistTracksScreen(
    playlistId: Long,
    playlistTitle: String,
    navController: NavController,
    deezerViewModel: DeezerViewModel = hiltViewModel()
) {
    val tracks = deezerViewModel.tracks.collectAsState().value
    val isLoading = deezerViewModel.isLoadingTracks.collectAsState().value
    val currentPlayingTrackId = deezerViewModel.currentPlayingTrackId.collectAsState().value
    val favoriteTrackIds = deezerViewModel.favoriteTrackIds.collectAsState().value

    LaunchedEffect(playlistId) {
        deezerViewModel.fetchPlaylistTracks(playlistId)
    }

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
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFE37EF3)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)
            ) {
                items(tracks) { track ->
                    TrackCard(
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

