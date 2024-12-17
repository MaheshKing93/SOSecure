package uk.ac.tees.mad.sosecure.presentation.screens

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay


@Composable
fun SOSActivationScreen(navController: NavController) {
    val context = LocalContext.current
    var countdownTime by remember { mutableIntStateOf(5) }
    var isCancelled by remember { mutableStateOf(false) }
    var emergencyContact by remember { mutableStateOf("") }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Load emergency contacts from Firestore
    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        db.collection("users").document(auth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener { document ->
                document?.let {
                    emergencyContact = it.get("emergencyNumber") as? String ?: ""
                    println(emergencyContact)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load emergency contacts.", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    // Countdown Logic
    LaunchedEffect(countdownTime) {
        while (countdownTime > 0 && !isCancelled) {
            delay(1000L)
            countdownTime--
        }
        if (countdownTime == 0 && !isCancelled) {
            sendSOSAlert(fusedLocationClient, emergencyContact, context, {
                navController.navigate("success")
            })
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


// Function to handle SOS Alert
@SuppressLint("MissingPermission")
private fun sendSOSAlert(
    fusedLocationClient: FusedLocationProviderClient,
    emergencyContacts: String,
    context: Context,
    onSent: () -> Unit
) {
    //Getting location
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude

            Log.d("SOS", "Latitude: $latitude, Longitude: $longitude")
            // Compose SOS message
            val message =
                "SOS! I need help. My location: https://maps.google.com/?q=$latitude,$longitude"

            // Send SMS to  emergency contacts
            sendSMS(emergencyContacts, message, context, { onSent() })

        } else {
            Toast.makeText(context, "Failed to fetch location.", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
    }
}

// Function to send SMS
private fun sendSMS(phoneNumber: String, message: String, context: Context, onSent: () -> Unit) {
//    val intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null))
//    intent.putExtra("sms_body", message)
//    context.startActivity(intent)
    try {
        val smsManager = context.getSystemService(SmsManager::class.java)
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        onSent()
    } catch (ex: Exception) {
        ex.printStackTrace()
        Toast.makeText(context, "SOS can't be sent.", Toast.LENGTH_SHORT).show()
    }
}
