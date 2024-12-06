package uk.ac.tees.mad.sosecure.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SOSActivationScreen(navController: NavController) {
    val context = LocalContext.current
    var countdownTime by remember { mutableIntStateOf(5) }
    var isCancelled by remember { mutableStateOf(false) }
    var emergencyContact by remember { mutableStateOf("") }



    // Countdown Logic
    LaunchedEffect(countdownTime) {
        while (countdownTime > 0 && !isCancelled) {
            delay(1000L)
            countdownTime--
        }
        if (countdownTime == 0 && !isCancelled) {
//            navController.popBackStack() // Navigate back after sending SOS
        }
    }

    // UI Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Title and Countdown
            Text(
                text = "Sending SOS Alert in",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = countdownTime.toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDD3E39)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "If this is a mistake, you can cancel now.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Send Now Button
                Button(
                    onClick = {
                        countdownTime = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD3E39)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Send Now", color = Color.White, fontWeight = FontWeight.Bold)
                }

                // Cancel Button
                Button(
                    onClick = {
                        isCancelled = true
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Cancel", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
