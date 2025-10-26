package com.senaaksoy.moodify.api

import com.senaaksoy.moodify.model.DeezerSearchResponse
import com.senaaksoy.moodify.model.DeezerTracksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApi {

    //playlist arama
    @GET("search/playlist")
    suspend fun searchPlaylists(
        @Query("q") mood: String,
        @Query("limit") limit: Int = 20
    ): DeezerSearchResponse

    // Playlist'teki Şarkıları Getir
    @GET("playlist/{playlistId}/tracks")
    suspend fun getPlaylistTracks(
        @Path("playlistId") playlistId: Long,
        @Query("limit") limit: Int = 50
    ): DeezerTracksResponse
}

