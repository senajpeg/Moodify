package com.senaaksoy.moodify.navigation

enum class Screen(val route : String) {
    SplashScreen(route = "SplashScreen"),
    SignInScreen(route = "SignInScreen"),
    SignUpScreen(route = "SignUpScreen"),
    HomeScreen(route = "HomeScreen"),
    PickMoodScreen(route = "PickMoodScreen"),
    ShowPlaylistScreen(route="ShowPlaylistScreen"),
    FavouritesScreen(route = "FavouritesScreen"),
    ForgotPasswordScreen(route = "ForgotPasswordScreen"),
    ResetPasswordScreen(route = "ResetPasswordScreen?oobCode={oobCode}")

}