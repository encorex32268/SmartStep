package com.lihan.smartstep.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lihan.smartstep.core.domain.Route
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.service.SmartStepForegroundService
import com.lihan.smartstep.dashboard.presentation.DashboardRoot
import com.lihan.smartstep.profile_setup.presentation.ProfileSetupRoot


class MainActivity : ComponentActivity() {

    override fun onPause() {
        val intent = Intent(this@MainActivity, SmartStepForegroundService::class.java).apply {
            action = SmartStepForegroundService.ACTION_START
            putExtra(SmartStepForegroundService.STEPS_KEY,1011)
            putExtra(SmartStepForegroundService.CALORIES_KEY,250)
            putExtra(SmartStepForegroundService.PROGRESS_KEY,60)
        }
        ContextCompat.startForegroundService(this@MainActivity, intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )

        setContent {
            val navController = rememberNavController()

            SmartStepTheme {
                NavHost(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = navController,
//                    startDestination = Route.ProfileSetup(isFromDashboard = false)
                    startDestination = Route.Dashboard
                ){
                    composable<Route.ProfileSetup>{
                        val isFromDashboard = it.toRoute<Route.ProfileSetup>().isFromDashboard
                        ProfileSetupRoot(
                            isFromDashboard = isFromDashboard,
                            onNavigateToDashboard = {
                                navController.navigate(Route.Dashboard){
                                    launchSingleTop = true
                                    popUpTo<Route.ProfileSetup>{
                                        inclusive = true
                                    }
                                }
                            },
                            onNavigateUp = {
                                navController.navigateUp()
                            }
                        )
                    }
                    composable<Route.Dashboard>{
                        DashboardRoot(
                            onExitApp = {
                                this@MainActivity.finishAffinity()
                            },
                            onNavigateToProfileSettings = {
                                navController.navigate(Route.ProfileSetup(isFromDashboard = true))
                            }
                        )
                    }

                    composable<Route.Catalog>{
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .safeDrawingPadding()
                        ){
                            CatalogScreen()
                        }
                    }
                }
            }
        }
    }
}

