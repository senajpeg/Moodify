package com.senaaksoy.moodify.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.components.EditTextField
import com.senaaksoy.moodify.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.PASSWORD_RESET_EMAIL_SENT -> {
                Toast.makeText(context, "Password reset link sent to your email.", Toast.LENGTH_SHORT).show()
                authViewModel.resetAuthState()
                navController.popBackStack()
            }
            AuthState.FAILURE -> {
                Toast.makeText(context, "Something went wrong. Try again.", Toast.LENGTH_SHORT).show()
                authViewModel.resetAuthState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF5D10A2), Color(0xFF6257E7))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Text("Enter your registered email", color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            EditTextField(
                value = authViewModel.inputEmail,
                onValueChange = { authViewModel.updateInputEmail(it) },
                label = R.string.email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { authViewModel.sendPasswordResetEmail() },
                enabled = authViewModel.isvalidEmail(),
                modifier = Modifier.width(220.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA065E3))
            ) {
                Text("Send Reset Link")
            }
        }
    }
}
