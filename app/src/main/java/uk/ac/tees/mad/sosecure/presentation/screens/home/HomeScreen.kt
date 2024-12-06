package uk.ac.tees.mad.sosecure.presentation.screens.home

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val safetyTips = listOf(
        "Always be aware of your surroundings.",
        "Share your travel plans with someone you trust.",
        "Carry a fully charged mobile phone."
    )

    val pagerState = rememberPagerState(initialPage = 0) { safetyTips.size }

    val permissions = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.SEND_SMS,
    )
    val permissionLauncher = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(true) {
        permissionLauncher.launchMultiplePermissionRequest()
        while (true) {
            delay(5000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % safetyTips.size)
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "SOSecure",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDD3E39),
                modifier = Modifier.padding(top = 40.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Safety Tips Section
//            Text(
//                text = "Safety Tips",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.Black
//            )
            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .border(
                        width = Dp.Hairline,
                        color = Color.Black,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = safetyTips[it],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center

                )
            }

            Spacer(modifier = Modifier.weight(1f))

            //SOS Button
            ElevatedCard(
                onClick = {
                    navController.navigate("sos_activation")
                },
                shape = CircleShape,
                modifier = Modifier
                    .size(200.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color(0xFFDD3E39),
                    contentColor = Color.White
                ),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "SOS",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }


}