package com.senaaksoy.moodify.model

data class DeezerSearchResponse(
    val data: List<DeezerPlaylist>,
    val total: Int
)
