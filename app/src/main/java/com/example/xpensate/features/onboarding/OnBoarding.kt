package com.example.xpensate.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xpensate.R
import com.example.xpensate.ui.theme.Zinc

@Composable
fun OnboardingScreen(onNavigate: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7F6))
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.onboarding_img),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Spend Smarter\nSave More",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Zinc
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onNavigate()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Zinc
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Get Started")
            }
        }
    }
}