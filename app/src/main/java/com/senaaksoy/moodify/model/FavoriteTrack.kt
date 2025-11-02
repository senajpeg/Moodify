package com.senaaksoy.moodify.model

data class FavoriteTrack(
    val trackId: Long = 0L,
    val title: String = "",
    val artistName: String = "",
    val artistId: Long = 0L,
    val albumTitle: String = "",
    val albumId: Long = 0L,
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
        artistId = this.artist.id,
        albumTitle = this.album.title,
        albumId = this.album.id,
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
            id = this.artistId,
            name = this.artistName,
            pictureMedium = this.albumCover
        ),
        album = DeezerAlbum(
            id = this.albumId,
            title = this.albumTitle,
            coverMedium = this.albumCover
        )
    )
}