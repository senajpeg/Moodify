package com.senaaksoy.moodify.model

data class FavoriteTrack(
    val trackId: Long = 0L,
    val title: String = "",
    val artistName: String = "",
    val albumTitle: String = "",
    val albumCover: String? = null,
    val duration: Int = 0,
    val preview: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)

fun DeezerTrack.toFavoriteTrack(): FavoriteTrack {
    return FavoriteTrack(
        trackId = this.id,
        title = this.title,
        artistName = this.artist.name,
        albumTitle = this.album.title,
        albumCover = this.album.coverMedium,
        duration = this.duration,
        preview = this.preview
    )
}

fun FavoriteTrack.toDeezerTrack(): DeezerTrack {
    return DeezerTrack(
        id = this.trackId,
        title = this.title,
        duration = this.duration,
        preview = this.preview,
        artist = DeezerArtist(
            id = 0L,
            name = this.artistName,
            pictureMedium = null
        ),
        album = DeezerAlbum(
            id = 0L,
            title = this.albumTitle,
            coverMedium = this.albumCover
        )
    )
}