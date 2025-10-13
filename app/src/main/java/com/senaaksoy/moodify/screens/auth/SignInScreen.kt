package com.senaaksoy.moodify.screens.auth


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.components.EditTextField
import com.senaaksoy.moodify.navigation.Screen

@Composable
fun SignInScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
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
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
                label = R.string.password,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFFcfccf0)
                    )
                },
                trailingIcon = {},
                colors = OutlinedTextFieldDefaults.colors(focusedLabelColor = Color.White),

                )

            Text(
                text = stringResource(R.string.forgot_password),
                color = Color(0xFFaea0e4),
                modifier = modifier
                    .width(270.dp)
                    .clickable {},
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
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
                )

            ) {
                Text(text = stringResource(R.string.login))
            }

            Button(
                onClick = {},
                modifier = modifier
                    .width(224.dp)
                    .clip(shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.google),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = modifier
                        .width(18.dp)
                        .height(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.continue_with_google)
                )
            }

            TextButton(
                onClick = {navController.navigate(Screen.SignUpScreen.route)}
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
