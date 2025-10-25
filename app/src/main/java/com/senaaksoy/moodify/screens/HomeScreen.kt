package com.senaaksoy.moodify.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.senaaksoy.moodify.R
import com.senaaksoy.moodify.navigation.Screen


@Composable
fun HomeScreen(
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9C27B0),
                    style = TextStyle(
                        drawStyle = Stroke(width = 24f)
                    )
                )
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = stringResource(R.string.title),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(
                    brush=Brush.linearGradient(colors =
                        listOf(
                            Color(0xFFD068E0),
                            Color(0xFF4AABF5)
                        )
                    )
                ),
                modifier = modifier.padding(36.dp)
            )
            Icon(
                painter = painterResource(R.drawable.emoji),
                tint = Color.Unspecified,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .widthIn(max = 200.dp)

            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {navController.navigate(Screen.PickMoodScreen.route)},
                modifier = modifier
                    .fillMaxWidth(0.65f)
                    .widthIn(max = 300.dp)
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
                Text(text = stringResource(R.string.get_started))
            }
        }

    }

}


@Preview
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}