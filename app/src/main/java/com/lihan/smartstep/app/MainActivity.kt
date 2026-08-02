package com.lihan.smartstep.app

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import androidx.core.app.ServiceCompat.stopForeground
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lihan.smartstep.core.data.hasActivityRecognitionPermission
import com.lihan.smartstep.core.domain.Route
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.service.SmartStepForegroundService
import com.lihan.smartstep.dashboard.presentation.DashboardRoot
import com.lihan.smartstep.dashboard.presentation.aicoach.AICoachRoot
import com.lihan.smartstep.profile_setup.presentation.ProfileSetupRoot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {


    override fun onPause() {
        super.onPause()
        if (!this@MainActivity.hasActivityRecognitionPermission) return
        val intent = Intent(this@MainActivity, SmartStepForegroundService::class.java).apply {
            action = SmartStepForegroundService.ACTION_START
        }
        //start foreground service
        ContextCompat.startForegroundService(this@MainActivity, intent)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this@MainActivity, SmartStepForegroundService::class.java).apply {
            action = SmartStepForegroundService.ACTION_STOP
        }
        //safe close foreground service
        startService(intent)
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
                            },
                            onNavigateToAICoach = {
                                navController.navigate(Route.AICoach)
                            }
                        )
                    }

                    composable<Route.AICoach>{
                        AICoachRoot(
                            onBack = {
                                navController.navigateUp()
                            }
                        )
                    }
                }
            }
        }
    }
}

