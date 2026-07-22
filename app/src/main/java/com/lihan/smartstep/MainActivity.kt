package com.lihan.smartstep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundMain
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.dashboard.presentation.DashboardRoot
import com.lihan.smartstep.profile_setup.presentation.ProfileSetupRoot
import com.lihan.smartstep.profile_setup.presentation.ProfileSetupScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object ProfileSetup: Route

    @Serializable
    data object Dashboard: Route
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = BackgroundMain.toArgb(),
                darkScrim = BackgroundMain.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            val navController = rememberNavController()
            SmartStepTheme {
                NavHost(
                    navController = navController,
                    startDestination = Route.Dashboard
                ){
                    composable<Route.ProfileSetup>{
                        ProfileSetupRoot(
                            onNavigateToDashboard = {
                                navController.navigate(Route.Dashboard){
                                    launchSingleTop = true
                                    popUpTo<Route.Dashboard>{
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable<Route.Dashboard>{
                        DashboardRoot()
                    }



                }

            }
        }
    }
}

