package com.senaaksoy.moodify.repository

import com.senaaksoy.moodify.api.DeezerApi
import com.senaaksoy.moodify.model.DeezerSearchResponse
import com.senaaksoy.moodify.model.DeezerTracksResponse
import javax.inject.Inject

class DeezerRepository @Inject constructor(
    private val api: DeezerApi
) {
    suspend fun getPlaylistsByMood(mood: String): DeezerSearchResponse {
        return api.searchPlaylists(mood = mood)
    }

    suspend fun getPlaylistTracks(playlistId: Long): DeezerTracksResponse {
        return api.getPlaylistTracks(playlistId = playlistId)
    }
}