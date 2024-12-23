package uk.ac.tees.mad.sosecure.presentation.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
        android.Manifest.permission.VIBRATE
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
        ) {

            Text(
                text = "SOSecure",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDD3E39),
                modifier = Modifier.padding(top = 50.dp)
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
                    .padding(16.dp)
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

            BottomBar(navController)
        }
    }


}

@Composable
fun BottomBar(navController: NavController) {
    val route = navController.currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFFDD3E39)
    ) {
        NavigationBarItem(
            selected = route == "home",
            onClick = {
                navController.navigate("home")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Sos,
                    contentDescription = "Sos",
                    tint = Color(0xFFDD3E39)
                )
            },
            label = {
                Text(
                    text = "SOS",
                    color = Color(0xFFDD3E39),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFDD3E39),
                selectedTextColor = Color(0xFFDD3E39),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = route == "profile",
            onClick = {
                navController.navigate("profile")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color(0xFFDD3E39)
                )
            },
            label = {
                Text(
                    text = "Profile",
                    color = Color(0xFFDD3E39),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFDD3E39),
                selectedTextColor = Color(0xFFDD3E39),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}