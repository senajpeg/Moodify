package com.senaaksoy.moodify.screens.splash


import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
    // İlk açılışta bildirim izni iste
    RequestNotificationPermission()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        // Eğer uygulama bir deep link ile açıldıysa, intent.data üzerinden oku
        val data = (context as? Activity)?.intent?.data
        val host = data?.host
        val oobCode = data?.getQueryParameter("oobCode")

        if (host == "resetPassword" && !oobCode.isNullOrEmpty()) {
            // Reset password ekranına git
            /*navController.navigate("ResetPasswordScreen?oobCode=$oobCode") {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }*/
            navController.navigateSingleTopClear(route = "ResetPasswordScreen?oobCode=$oobCode")
        } else {
            // Normal 3 saniye bekleyip SignInScreen'e git
            delay(3000)
           /* navController.navigate(Screen.SignInScreen.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }*/
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
