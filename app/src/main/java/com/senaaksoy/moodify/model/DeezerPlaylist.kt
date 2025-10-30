package com.senaaksoy.moodify.model

import com.google.gson.annotations.SerializedName

data class DeezerSearchResponse(
    val data: List<DeezerPlaylist>,
    val total: Int
)


data class DeezerPlaylist(
    val id: Long,
    val title: String,
    val description: String? = null,
    val link: String,
    @SerializedName("picture") val picture: String? = null,
    @SerializedName("picture_medium") val pictureMedium: String? = null,
    @SerializedName("picture_big") val pictureBig: String? = null,
    @SerializedName("nb_tracks") val nbTracks: Int? = null, //şarkı sayısı
    val fans: Int? = null //fan sayısı
)

data class DeezerTrack(
    val id: Long,
    val title: String, //şarkı adı
    val duration: Int,  // saniye cinsinden
    val preview: String? = null,  // 30 saniyelik preview URL
    val artist: DeezerArtist,
    val album: DeezerAlbum
)

data class DeezerArtist(
    val id: Long,
    val name: String,
    @SerializedName("picture_medium") val pictureMedium: String? = null
)

data class DeezerAlbum(
    val id: Long,
    val title: String,
    @SerializedName("cover_medium") val coverMedium: String? = null
)

data class DeezerTracksResponse(
    val data: List<DeezerTrack> //playlist listesi
)
