package com.senaaksoy.moodify.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.senaaksoy.moodify.components.MoodifyBottomBar
import com.senaaksoy.moodify.components.MoodifyTopBar
import com.senaaksoy.moodify.components.shouldShowBottomBar
import com.senaaksoy.moodify.screens.auth.ForgotPasswordScreen
import com.senaaksoy.moodify.screens.auth.ResetPasswordScreen
import com.senaaksoy.moodify.screens.auth.SignInScreen
import com.senaaksoy.moodify.screens.auth.SignUpScreen
import com.senaaksoy.moodify.screens.favorites.FavouritesScreen
import com.senaaksoy.moodify.screens.main.home.HomeScreen
import com.senaaksoy.moodify.screens.main.mood.PickMoodScreen
import com.senaaksoy.moodify.screens.main.playlist.PlaylistTracksScreen
import com.senaaksoy.moodify.screens.main.playlist.ShowPlaylistScreen
import com.senaaksoy.moodify.screens.main.profile.ProfileScreen
import com.senaaksoy.moodify.screens.splash.SplashScreen

@Composable
fun MoodifyNavigation() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route ?: Screen.HomeScreen.route

    val showBottomBar = shouldShowBottomBar(currentRoute)


    Scaffold(
        topBar = {
            when {
                currentRoute.startsWith("ShowPlaylistScreen") -> {
                    val mood = currentBackStackEntry?.arguments?.getString("mood") ?: "Playlists"
                    MoodifyTopBar(title = mood, navController = navController)
                }
                currentRoute.startsWith("PlaylistTracksScreen") -> {
                    val title = currentBackStackEntry?.arguments?.getString("playlistTitle") ?: "Tracks"
                    MoodifyTopBar(title = title, navController = navController)
                }
                else -> {}
            }
        },
        bottomBar = {
            if (showBottomBar) {
                MoodifyBottomBar(navController = navController,
                    currentRoute = currentRoute)
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Screen.SplashScreen.route
        ) {
            composable(route = Screen.SplashScreen.route) {
                SplashScreen(navController = navController)
            }
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(navController = navController)
            }
            composable(route = Screen.SignInScreen.route) {
                SignInScreen(navController = navController)
            }
            composable(route = Screen.SignUpScreen.route) {
                SignUpScreen(navController=navController)
            }
            composable(route = Screen.ProfileScreen.route) {
                ProfileScreen(navController=navController)
            }
            composable(
                route = Screen.PlaylistTracksScreen.route,
                arguments = listOf(
                    navArgument("playlistId") {
                        type = NavType.LongType
                    },
                    navArgument("playlistTitle") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
                val playlistTitle = backStackEntry.arguments?.getString("playlistTitle") ?: ""
                PlaylistTracksScreen(
                    playlistId = playlistId,
                    playlistTitle = playlistTitle,
                    navController = navController
                )
            }
            composable(route = Screen.PickMoodScreen.route) {
                PickMoodScreen(navController=navController)
            }
            composable(
                route = Screen.ShowPlaylistScreen.route,
                arguments = listOf(
                    navArgument("mood") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val mood = backStackEntry.arguments?.getString("mood") ?: ""
                ShowPlaylistScreen(
                    mood = mood,
                    navController = navController
                )
            }
            composable(route = Screen.FavouritesScreen.route) {
                FavouritesScreen(navController = navController)
            }
            composable(route = Screen.ForgotPasswordScreen.route) {
                ForgotPasswordScreen(navController = navController)
            }
            composable(route = Screen.ResetPasswordScreen.route) { backStackEntry ->
                val oobCode = backStackEntry.arguments?.getString("oobCode")
                ResetPasswordScreen(navController = navController, oobCode = oobCode)
            }

        }
    }


}