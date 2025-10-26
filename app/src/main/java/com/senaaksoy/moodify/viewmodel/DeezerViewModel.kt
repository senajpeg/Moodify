package com.senaaksoy.moodify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senaaksoy.moodify.model.DeezerPlaylist
import com.senaaksoy.moodify.model.DeezerTrack
import com.senaaksoy.moodify.repository.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeezerViewModel @Inject constructor(
    private val repository: DeezerRepository
) : ViewModel() {

    private val _selectedMood = MutableStateFlow<String?>(null)
    val selectedMood: StateFlow<String?> = _selectedMood.asStateFlow()

    fun selectMood(mood: String) {
        _selectedMood.value = mood
    }

    private val _playlists = MutableStateFlow<List<DeezerPlaylist>>(emptyList())
    val playlists: StateFlow<List<DeezerPlaylist>> = _playlists.asStateFlow()

    private val _tracks = MutableStateFlow<List<DeezerTrack>>(emptyList())
    val tracks: StateFlow<List<DeezerTrack>> = _tracks.asStateFlow()

    private val _isLoadingPlaylists = MutableStateFlow(false)
    val isLoadingPlaylists: StateFlow<Boolean> = _isLoadingPlaylists.asStateFlow()

    private val _isLoadingTracks = MutableStateFlow(false)
    val isLoadingTracks: StateFlow<Boolean> = _isLoadingTracks.asStateFlow()

    fun fetchPlaylists(mood: String) {
        viewModelScope.launch {
            _isLoadingPlaylists.value = true
            try {
                val response = repository.getPlaylistsByMood(mood)
                _playlists.value = response.data
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
}