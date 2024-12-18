package uk.ac.tees.mad.sosecure.presentation.screens


import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun EditProfileScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var userName by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf("") }

    // Load current user details
    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid ?: return@LaunchedEffect
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userName = document.getString("name") ?: ""
                    userPhone = document.getString("phone") ?: ""
                    emergencyContact = document.getString("emergencyNumber") ?: ""
                }
            }
            .addOnFailureListener {
                Log.e("EditProfileScreen", "Error fetching user data: ${it.message}")
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDD3E39),
                modifier = Modifier.padding(top = 50.dp)
            )


            // User Name Input
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Name") },
                placeholder = { Text("Enter your name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFDD3E39),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color(0xFFDD3E39),
                    unfocusedTextColor = Color(0xFFDD3E39),
                )
            )

            // User Phone Input
            OutlinedTextField(
                value = userPhone,
                onValueChange = { userPhone = it },
                label = { Text("Phone Number") },
                placeholder = { Text("Enter your phone number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFDD3E39),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color(0xFFDD3E39),
                    unfocusedTextColor = Color(0xFFDD3E39),
                )
            )

            // Emergency Contact Input
            OutlinedTextField(
                value = emergencyContact,
                onValueChange = { emergencyContact = it },
                label = { Text("Emergency Contact") },
                placeholder = { Text("Enter emergency contact") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFDD3E39),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color(0xFFDD3E39),
                    unfocusedTextColor = Color(0xFFDD3E39),
                )
            )
            // Save Changes Button
            Button(
                onClick = {
                    val userId = auth.currentUser?.uid ?: return@Button
                    val updatedData = mapOf(
                        "name" to userName,
                        "phone" to userPhone,
                        "emergencyContact" to emergencyContact
                    )
                    db.collection("users").document(userId).update(updatedData)
                        .addOnSuccessListener {
                            navController.popBackStack() // Navigate back
                        }
                        .addOnFailureListener {
                            Log.e("EditProfileScreen", "Error updating user data: ${it.message}")
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD3E39))
            ) {
                Text("Save Changes", color = Color.White)
            }
        }
    }
}
