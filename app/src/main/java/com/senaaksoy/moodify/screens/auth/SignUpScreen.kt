package com.senaaksoy.moodify.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.components.CustomDialog
import com.senaaksoy.moodify.components.EditTextField
import com.senaaksoy.moodify.navigation.Screen
import com.senaaksoy.moodify.viewmodel.AuthViewModel



@Composable
fun SignUpScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel= hiltViewModel()
) {

    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()


    LaunchedEffect(authState) {
        when (authState) {
            AuthState.USER_ALREADY_EXISTS -> {
                Toast.makeText(context, "This user already exists", Toast.LENGTH_SHORT).show()
                authViewModel.resetAuthState()
            }
            else -> {}
        }
    }

    if (authState == AuthState.SUCCESS) {
        CustomDialog(
            title = "Registration Successful",
            message = "Please click on the verification link sent to your email address.",
            onDismiss = {
                authViewModel.resetAuthState()
                navController.navigate(Screen.SignInScreen.route)
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF5D10A2),
                        Color(0xFF6257E7)
                    ),
                )
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.moodify_logo),
                tint = Color.Unspecified,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.6f)

            )
            EditTextField(
                value = authViewModel.inputUsername,
                onValueChange = { authViewModel.updateUsername(it) },
                label = R.string.username,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFFcfccf0)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(focusedLabelColor = Color.White),
                supportingText = if (authViewModel.usernameSupportText()) R.string.username_support_text else null

            )
            EditTextField(
                value = authViewModel.inputEmail,
                onValueChange = {authViewModel.updateInputEmail(it)},
                label = R.string.email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFFcfccf0)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(focusedLabelColor = Color.White),
                supportingText = if (authViewModel.emailSupportText()) R.string.email_support_text else null
            )
            EditTextField(
                value = authViewModel.inputPassword,
                onValueChange = {authViewModel.updateInputPassword(it)},
                label = R.string.password,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFFcfccf0)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if(authViewModel.passwordVisibility)Icons.Filled.Visibility else Icons.Filled.VisibilityOff ,
                        contentDescription = null,
                        modifier = modifier.clickable {
                            authViewModel.passwordVisibility = !authViewModel.passwordVisibility
                        }
                    )
                },
                visualTransformation = if (authViewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(focusedLabelColor = Color.White),
                supportingText = if (authViewModel.passwordSupportText()) R.string.password_support else null

                )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {authViewModel.signUp()},
                enabled = authViewModel.isvalid(),
                modifier = modifier
                    .width(224.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFA065E3),
                                Color(0xFF5E8BCB)
                            )
                        )
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),

            ) {
                Text(text = stringResource(R.string.sign_up))
            }

            TextButton(
                onClick = {navController.navigate(Screen.SignInScreen.route)}
            ) {
                Text(
                    text = stringResource(R.string.already_have_an_account),
                    color = Color(0xFFaea0e4),
                )
            }

        }

    }
}


@Preview
@Composable
fun SignUpPreview() {
    val navController = rememberNavController()
    SignUpScreen(navController = navController)
}