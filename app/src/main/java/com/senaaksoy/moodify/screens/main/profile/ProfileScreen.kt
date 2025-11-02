package com.senaaksoy.moodify.screens.main.profile


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.navigation.Screen
import com.senaaksoy.moodify.navigation.navigateSingleTopClear
import com.senaaksoy.moodify.utils.RequestNotificationPermission
import com.senaaksoy.moodify.viewmodel.AuthViewModel
import com.senaaksoy.moodify.viewmodel.NotificationViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    // Bildirim izni iste
    RequestNotificationPermission()

    val currentUser = authViewModel.currentUser
    val selectedImageUrl by authViewModel.selectedImageUrl.collectAsState()
    val isUploadingImage by authViewModel.isUploadingImage.collectAsState()
    val notificationsEnabled by notificationViewModel.notificationsEnabled.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { authViewModel.uploadProfileImage(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A0B2E),
                        Color(0xFF2D1B55)
                    )
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .clickable(enabled = !isUploadingImage) {
                    launcher.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            when {
                isUploadingImage -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = Color.White
                    )
                }
                selectedImageUrl != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUrl),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Profile Image",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        ProfileInfoRow(
            label = stringResource(R.string.username_),
            value = currentUser?.displayName ?: stringResource(R.string.n_a)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileInfoRow(
            label = stringResource(R.string.email_),
            value = currentUser?.email ?: stringResource(R.string.n_a)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Divider(
            color = Color.White.copy(alpha = 0.2f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 16.dp)
        )


        NotificationSettingsRow(
            enabled = notificationsEnabled,
            onToggle = { notificationViewModel.toggleNotifications(it) }
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                authViewModel.logOut()
                navController.navigateSingleTopClear(Screen.SignInScreen.route)
            },
            modifier = Modifier
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
            Text(
                text = stringResource(R.string.log_out),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }


    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun NotificationSettingsRow(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Bildirimler",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Günlük ve haftalık ruh hali hatırlatmaları",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }

        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF6C63FF),
                checkedTrackColor = Color(0xFF6C63FF).copy(alpha = 0.5f),
                uncheckedThumbColor = Color.White.copy(alpha = 0.6f),
                uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )
    }
}