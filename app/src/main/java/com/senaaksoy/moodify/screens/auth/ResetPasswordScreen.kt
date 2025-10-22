package com.senaaksoy.moodify.screens.auth



import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.components.EditTextField
import com.senaaksoy.moodify.navigation.Screen
import com.senaaksoy.moodify.viewmodel.AuthViewModel

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    oobCode: String?,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by authViewModel.authState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler { navController.navigate(Screen.SignInScreen.route) }

    LaunchedEffect(uiState) {
        when (uiState) {
            AuthState.SUCCESS -> {
                authViewModel.showDialog = true
            }
            AuthState.FAILURE -> {
                Toast.makeText(context, "An error occurred.", Toast.LENGTH_SHORT).show()
                authViewModel.resetAuthState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF5D10A2), Color(0xFF6257E7))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )

            EditTextField(
                value = authViewModel.newPassword,
                onValueChange = { authViewModel.updateNewPassword(it) },
                label = R.string.new_password,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.LightGray) },
                trailingIcon = {
                    Icon(
                        imageVector = if (authViewModel.passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            authViewModel.passwordVisibility = !authViewModel.passwordVisibility
                        },
                        tint = Color.White
                    )
                },
                visualTransformation = if (authViewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
            )

            EditTextField(
                value = authViewModel.confirmPassword,
                onValueChange = { authViewModel.updateConfirmPassword(it) },
                label = R.string.confirm_password,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.LightGray) },
                trailingIcon = {
                    Icon(
                        imageVector = if (authViewModel.confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            authViewModel.confirmPasswordVisibility = !authViewModel.confirmPasswordVisibility
                        },
                        tint = Color.White
                    )
                },
                visualTransformation = if (authViewModel.confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (authViewModel.passwordsMatch() && oobCode != null) {
                        authViewModel.resetPassword(oobCode)
                    } else {
                        Toast.makeText(context, "Passwords must match.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .width(250.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFA065E3), Color(0xFF5E8BCB))
                        )
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(text = "Reset Password")
            }

            if (authViewModel.showDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Success") },
                    text = { Text("Your password has been successfully changed.") },
                    confirmButton = {
                        TextButton(onClick = {
                            authViewModel.showDialog = false
                            navController.navigate(Screen.SignInScreen.route)
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
