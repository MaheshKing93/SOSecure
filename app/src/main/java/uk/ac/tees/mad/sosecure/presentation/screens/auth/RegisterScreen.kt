package uk.ac.tees.mad.sosecure.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.sosecure.R

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    var emergencyNumber by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFFFFF), Color(0xFFDD3E39))
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Logo Image
            Column(
                modifier = Modifier
                    .width(190.dp)
                    .height(93.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                )
                Image(
                    painter = painterResource(id = R.drawable.sos),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            // Title Text
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Form Inputs
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name", color = Color.White) },
                placeholder = { Text("Enter your full name", color = Color.LightGray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                placeholder = { Text("Enter your email", color = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.White) },
                placeholder = { Text("Create a password", color = Color.LightGray) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password", color = Color.White) },
                placeholder = { Text("Re-enter your password", color = Color.LightGray) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = emergencyNumber,
                onValueChange = { emergencyNumber = it },
                label = { Text("Emergency Contact", color = Color.White) },
                placeholder = { Text("Enter emergency contact number", color = Color.LightGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Register Button
            Button(
                onClick = {
                    if (password.text == confirmPassword.text) {
                        firebaseAuth.createUserWithEmailAndPassword(email.text, password.text)
                            .addOnSuccessListener {
                                val user = firebaseAuth.currentUser

                                if (user != null) {
                                    val userData = hashMapOf(
                                        "name" to name.text,
                                        "email" to email.text,
                                        "emergencyNumber" to emergencyNumber.text
                                    )

                                    db.collection("users").document(user.uid)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Registration Successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate("login")
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                context,
                                                "Error: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }

                            }.addOnFailureListener { ex ->
                                val errorCode = (ex as FirebaseAuthException).errorCode
                                when (errorCode) {
                                    "ERROR_INVALID_EMAIL" -> Toast.makeText(
                                        context,
                                        "Invalid Email Format",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    "ERROR_EMAIL_ALREADY_IN_USE" -> Toast.makeText(
                                        context,
                                        "Email is already in use",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    "ERROR_WEAK_PASSWORD" -> Toast.makeText(
                                        context,
                                        "Password is too weak",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    else -> Toast.makeText(
                                        context,
                                        "Error: ${ex.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD3E39)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Register", color = Color.White, fontSize = 16.sp)
            }

            // Login Navigation
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Login", color = Color.White)
            }
        }
    }
}
