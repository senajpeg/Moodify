package com.senaaksoy.moodify.navigation

enum class Screen(val route : String) {
    SplashScreen(route = "SplashScreen"),
    SignInScreen(route = "SignInScreen"),
    SignUpScreen(route = "SignUpScreen"),
    HomeScreen(route = "HomeScreen"),
    PickMoodScreen(route = "PickMoodScreen"),
    ProfileScreen(route = "ProfileScreen"),
    ShowPlaylistScreen(route = "ShowPlaylistScreen/{mood}"),
    PlaylistTracksScreen(route = "PlaylistTracksScreen/{playlistId}/{playlistTitle}"),
    FavouritesScreen(route = "FavouritesScreen"),
    ForgotPasswordScreen(route = "ForgotPasswordScreen"),
    ResetPasswordScreen(route = "ResetPasswordScreen?oobCode={oobCode}");


    companion object {
        fun createShowPlaylistRoute(mood: String): String {
            return "ShowPlaylistScreen/$mood"
        }
        fun createPlaylistTracksRoute(playlistId: Long, playlistTitle: String): String {
            return "PlaylistTracksScreen/$playlistId/$playlistTitle"
        }
    }
}