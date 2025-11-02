package com.senaaksoy.moodify.screens.splash


import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.navigation.Screen
import com.senaaksoy.moodify.navigation.navigateSingleTopClear
import com.senaaksoy.moodify.utils.RequestNotificationPermission
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    RequestNotificationPermission()
    val context = LocalContext.current


    val view = LocalView.current

    DisposableEffect(Unit) {
        val window = (context as ComponentActivity).window
        val windowInsetsController = WindowCompat.getInsetsController(window, view)

        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        onDispose {
            windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
        }
    }

    LaunchedEffect(key1 = true) {
        // Eğer uygulama bir deep link ile açıldıysa, intent.data üzerinden oku
        val data = (context as? Activity)?.intent?.data
        val host = data?.host
        val oobCode = data?.getQueryParameter("oobCode")

        if (host == "resetPassword" && !oobCode.isNullOrEmpty()) {

            navController.navigateSingleTopClear(route = "ResetPasswordScreen?oobCode=$oobCode")
        } else {

            delay(3000)

            navController.navigateSingleTopClear(route = Screen.SignInScreen.route)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
