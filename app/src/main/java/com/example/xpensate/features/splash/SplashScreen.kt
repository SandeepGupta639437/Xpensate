package com.example.xpensate.features.splash

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(onNavigate: () -> Unit) {

    val alpha = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(true) {
        alpha.animateTo(1f, animationSpec = tween(1000))
        kotlinx.coroutines.delay(1500)
        onNavigate()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF6BAF92),
                        Color(0xFF3A7D73)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Xpensate",
            color = Color.White,
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.alpha(alpha.value)
        )
    }
}