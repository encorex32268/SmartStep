package com.lihan.smartstep

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lihan.smartstep.core.data.AppUserInfo
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.core.presentation.screens.profile.ProfileViewModel
import com.lihan.smartstep.onboarding.presentation.OnboardingProfileScreenRoot
import com.lihan.smartstep.stepcount.presentation.SmartStepScreenRoot
import com.lihan.smartstep.stepcount.presentation.SmartStepViewModel
import com.lihan.smartstep.stepcount.presentation.personalsettings.PersonalSettingsScreenRoot
import com.lihan.smartstep.ui.theme.SmartStepTheme
import kotlinx.serialization.Serializable

sealed interface Route{

    @Serializable
    data object Test: Route

    @Serializable
    data object OnboardingProfileSetting: Route

    @Serializable
    data object SmartStep: Route

    @Serializable
    data object PersonalSettings: Route

}

class MainActivity : ComponentActivity() {

    private val userInfoDataStore: UserInfoDataStore by lazy {
        AppUserInfo(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStepTheme {
                val navController = rememberNavController()


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = Route.SmartStep
                    ){
                        composable<Route.Test>{
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Button(
                                    onClick = {
                                        val context  = this@MainActivity
                                        val packageName = context.packageName
                                        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager

                                        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                                            val intent = Intent().apply {
                                                action = ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                                                data = Uri.parse("package:$packageName")
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            context.startActivity(intent)
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "Open"
                                    )
                                }
                            }
                        }

                        composable<Route.OnboardingProfileSetting>{
                            val viewModel = viewModel {
                                ProfileViewModel(
                                    userInfoDataStore = userInfoDataStore
                                )
                            }
                            OnboardingProfileScreenRoot(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                viewModel = viewModel,
                                onNavigateToSmartStep = {
                                    navController.navigate(Route.SmartStep)
                                }
                            )
                        }
                        composable<Route.SmartStep>{
                            val viewModel = viewModel {
                                SmartStepViewModel(
                                    userInfoDataStore = userInfoDataStore
                                )
                            }
                            SmartStepScreenRoot(
                                viewModel = viewModel,
                                onExit = {
                                    this@MainActivity.finish()
                                },
                                onNavigateToPersonSettings = {
                                    navController.navigate(Route.PersonalSettings)
                                }
                            )
                        }
                        composable<Route.PersonalSettings>{
                            val viewModel = viewModel {
                                ProfileViewModel(
                                    userInfoDataStore = userInfoDataStore
                                )
                            }
                            PersonalSettingsScreenRoot(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                viewModel = viewModel,
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
}
