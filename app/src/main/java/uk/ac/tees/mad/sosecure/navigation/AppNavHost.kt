package uk.ac.tees.mad.sosecure.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.sosecure.presentation.screens.SOSActivationScreen
import uk.ac.tees.mad.sosecure.presentation.screens.auth.LoginScreen
import uk.ac.tees.mad.sosecure.presentation.screens.auth.RegisterScreen
import uk.ac.tees.mad.sosecure.presentation.screens.home.HomeScreen
import uk.ac.tees.mad.sosecure.presentation.screens.splash.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("sos_activation") { SOSActivationScreen(navController) }
    }
}
