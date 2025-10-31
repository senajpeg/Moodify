package com.senaaksoy.moodify.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senaaksoy.moodify.model.DeezerPlaylist
import com.senaaksoy.moodify.model.DeezerTrack
import com.senaaksoy.moodify.model.FavoriteTrack
import com.senaaksoy.moodify.model.toFavoriteTrack
import com.senaaksoy.moodify.repository.DeezerRepository
import com.senaaksoy.moodify.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeezerViewModel @Inject constructor(
    private val repository: DeezerRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    init {
        loadFavorites()
    }

    //mod seçimi verileri
    private val _selectedMood = MutableStateFlow<String?>(null)
    val selectedMood: StateFlow<String?> = _selectedMood.asStateFlow()

    fun selectMood(mood: String) { _selectedMood.value = mood
        saveMoodToFirebase(mood) }

    private fun saveMoodToFirebase(mood: String) {
        viewModelScope.launch {
            try {
                repository.saveMood(mood)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //PLAYLIST & TRACK VERİLERİ
    private val _playlists = MutableStateFlow<List<DeezerPlaylist>>(emptyList())
    val playlists: StateFlow<List<DeezerPlaylist>> = _playlists.asStateFlow()

    private val _tracks = MutableStateFlow<List<DeezerTrack>>(emptyList())
    val tracks: StateFlow<List<DeezerTrack>> = _tracks.asStateFlow()

    private val _isLoadingPlaylists = MutableStateFlow(false)
    val isLoadingPlaylists: StateFlow<Boolean> = _isLoadingPlaylists.asStateFlow()

    private val _isLoadingTracks = MutableStateFlow(false)
    val isLoadingTracks: StateFlow<Boolean> = _isLoadingTracks.asStateFlow()

    //FAVORİLER
    private val _favoriteTrackIds = MutableStateFlow<Set<Long>>(emptySet())
    val favoriteTrackIds: StateFlow<Set<Long>> = _favoriteTrackIds.asStateFlow()

    private val _favorites = MutableStateFlow<List<FavoriteTrack>>(emptyList())
    val favorites: StateFlow<List<FavoriteTrack>> = _favorites.asStateFlow()

    //MÜZİK OYNATMA (MediaPlayer)
    private var mediaPlayer: MediaPlayer? = null
    private val _currentPlayingTrackId = MutableStateFlow<Long?>(null)
    val currentPlayingTrackId: StateFlow<Long?> = _currentPlayingTrackId.asStateFlow()

    private fun loadFavorites() {
        viewModelScope.launch {
            favoritesRepository.getFavorites().collect { favoritesList ->
                _favorites.value = favoritesList
                _favoriteTrackIds.value = favoritesList.map { it.trackId }.toSet()
            }
        }
    }

    fun toggleFavorite(track: DeezerTrack) {
        viewModelScope.launch {
            val isFavorite = _favoriteTrackIds.value.contains(track.id)
            if (isFavorite) {
                favoritesRepository.removeFromFavorites(track.id)
            } else {
                favoritesRepository.addToFavorites(track.toFavoriteTrack())
            }
        }
    }

    fun fetchPlaylists(mood: String) {
        viewModelScope.launch {
            _isLoadingPlaylists.value = true
            try {
                val englishResponse = repository.getPlaylistsByMood(mood)

                val turkishMood = when (mood.lowercase()) {
                    "happy" -> "mutlu"
                    "sad" -> "üzgün"
                    "chill" -> "sakin"
                    "energetic" -> "enerjik"
                    else -> mood
                }
                val turkishResponse = repository.getPlaylistsByMood(turkishMood)
                val combined = (englishResponse.data + turkishResponse.data)
                    .distinctBy { it.id }

                _playlists.value = combined
            } catch (e: Exception) {
                _playlists.value = emptyList()
            } finally {
                _isLoadingPlaylists.value = false
            }
        }
    }

    fun fetchPlaylistTracks(playlistId: Long) {
        viewModelScope.launch {
            _isLoadingTracks.value = true
            try {
                val response = repository.getPlaylistTracks(playlistId)
                _tracks.value = response.data
            } catch (e: Exception) {
                _tracks.value = emptyList()
            } finally {
                _isLoadingTracks.value = false
            }
        }
    }

    fun playPauseTrack(track: DeezerTrack) {
        if (_currentPlayingTrackId.value == track.id) {
            mediaPlayer?.pause()
            _currentPlayingTrackId.value = null
        } else {
            stopCurrentTrack()

            track.preview?.let { previewUrl ->
                try {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(previewUrl)
                        prepareAsync()
                        setOnPreparedListener {
                            start()
                            _currentPlayingTrackId.value = track.id
                        }
                        setOnCompletionListener {
                            _currentPlayingTrackId.value = null
                        }
                        setOnErrorListener { _, _, _ ->
                            _currentPlayingTrackId.value = null
                            true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _currentPlayingTrackId.value = null
                }
            }
        }
    }

    private fun stopCurrentTrack() {
        mediaPlayer?.release()
        mediaPlayer = null
        _currentPlayingTrackId.value = null
    }

    override fun onCleared() {
        super.onCleared()
        stopCurrentTrack()
    }
}