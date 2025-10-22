package com.senaaksoy.moodify.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.senaaksoy.moodify.screens.FavouritesScreen
import com.senaaksoy.moodify.screens.HomeScreen
import com.senaaksoy.moodify.screens.PickMoodScreen
import com.senaaksoy.moodify.screens.ShowPlaylistScreen
import com.senaaksoy.moodify.screens.auth.ForgotPasswordScreen
import com.senaaksoy.moodify.screens.auth.ResetPasswordScreen

import com.senaaksoy.moodify.screens.auth.SignInScreen
import com.senaaksoy.moodify.screens.auth.SignUpScreen
import com.senaaksoy.moodify.screens.splash.SplashScreen

@Composable
fun MoodifyNavigation() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route ?: Screen.HomeScreen.route

    Scaffold(
        topBar = {}
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues)
                ,
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
            composable(route = Screen.PickMoodScreen.route) {
                PickMoodScreen(navController=navController)
            }
            composable(route = Screen.ShowPlaylistScreen.route) {
                ShowPlaylistScreen(navController = navController)
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