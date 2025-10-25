package com.senaaksoy.moodify.screens.auth


import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.components.EditTextField
import com.senaaksoy.moodify.navigation.Screen
import com.senaaksoy.moodify.navigation.navigateSingleTopClear
import com.senaaksoy.moodify.viewmodel.AuthViewModel

@Composable
fun SignInScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val context = LocalContext.current

// Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let {
                authViewModel.signInWithGoogle(it)
            }
        } catch (e: ApiException) {
            Toast.makeText(
                context,
                "Google Sign-In başarısız: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    LaunchedEffect(authState) {
        when (authState) {
            AuthState.INVALID_EMAIL_OR_PASSWORD -> {
                Toast.makeText(
                    context, "\n" +
                            "Email or password is incorrect.", Toast.LENGTH_SHORT
                ).show()
                authViewModel.resetAuthState()
            }

            AuthState.INVALID_CREDENTIALS -> {
                Toast.makeText(
                    context, "\n" +
                            "Email or password is incorrect", Toast.LENGTH_SHORT
                ).show()
                authViewModel.resetAuthState()
            }

            AuthState.FAILURE -> {
                Toast.makeText(context, "An error has occurred.", Toast.LENGTH_SHORT).show()
                authViewModel.resetAuthState()
            }

            AuthState.EMAIL_NOT_VERIFIED -> {
                Toast.makeText(context, "Please verify your E-mail", Toast.LENGTH_SHORT).show()
                authViewModel.resetAuthState()
            }

            AuthState.SUCCESS -> {
                navController.navigateSingleTopClear(route = Screen.HomeScreen.route)
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
                    colors = listOf(
                        Color(0xFF5D10A2),
                        Color(0xFF6257E7)
                    ),
                )
            )
    ) {
        Column(
            modifier = Modifier
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
                value = authViewModel.inputEmail,
                onValueChange = { authViewModel.updateInputEmail(it) },
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
                colors = OutlinedTextFieldDefaults.colors(focusedLabelColor = Color.White)
            )
            EditTextField(
                value = authViewModel.inputPassword,
                onValueChange = { authViewModel.updateInputPassword(it) },
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
                        imageVector = if (authViewModel.passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        modifier = modifier.clickable {
                            authViewModel.passwordVisibility = !authViewModel.passwordVisibility
                        }
                    )
                },
                visualTransformation = if (authViewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(focusedLabelColor = Color.White),

                )

            Text(
                text = stringResource(R.string.forgot_password),
                color = Color(0xFFaea0e4),
                modifier = Modifier
                   // .width(270.dp)
                    .widthIn(max = 280.dp)
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.ForgotPasswordScreen.route) },
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { authViewModel.signIn() },
                enabled = authViewModel.isValidSignIn(),
                modifier = Modifier
                    //.width(224.dp)

                    .widthIn(max = 350.dp)
                    .fillMaxWidth(0.6f)
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
                )

            ) {
                Text(text = stringResource(R.string.login))
            }

            Button(
                onClick = {

                    authViewModel.startGoogleSignIn {
                        googleSignInLauncher.launch(authViewModel.getGoogleSignInIntent())
                    }
                    
                },
                modifier = Modifier
                   // .width(224.dp)

                    .widthIn(max = 350.dp)
                    .fillMaxWidth(0.6f)
                    .clip(shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.google),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.continue_with_google)
                )
            }

            TextButton(
                onClick = { navController.navigate(Screen.SignUpScreen.route) }
            ) {
                Text(
                    text = stringResource(R.string.have_an_account_sign_up),
                    color = Color(0xFFaea0e4),
                )
            }


        }


    }


}

@Preview
@Composable
fun SignInPreview() {
    val navController = rememberNavController()
    SignInScreen(navController = navController)
}
