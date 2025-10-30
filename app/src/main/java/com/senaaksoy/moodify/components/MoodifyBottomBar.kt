package com.senaaksoy.moodify.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.senaaksoy.moodify.navigation.Screen

fun shouldShowBottomBar(currentRoute: String): Boolean {
    val screensWithoutBottomBar = listOf(
        Screen.SplashScreen.route,
        Screen.SignInScreen.route,
        Screen.SignUpScreen.route,
        Screen.ForgotPasswordScreen.route
    )

    return !screensWithoutBottomBar.contains(currentRoute) &&
            !currentRoute.startsWith("ResetPasswordScreen")
}

@Composable
fun MoodifyBottomBar(
    navController: NavController,
    currentRoute: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF9C27B0),
                        Color(0xFF2196F3)
                    )
                )
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null
                    )
                },
                selected = currentRoute == Screen.HomeScreen.route,
                onClick = {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.White.copy(alpha = 0.2f)
                )
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null
                    )
                },
                selected = currentRoute == Screen.FavouritesScreen.route,
                onClick = {
                    navController.navigate(Screen.FavouritesScreen.route) {
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.White.copy(alpha = 0.2f)
                )
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                },
                selected = currentRoute == Screen.ProfileScreen.route,
                onClick = {
                    navController.navigate(Screen.ProfileScreen.route) {
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.White.copy(alpha = 0.2f)
                )
            )
        }
    }
}