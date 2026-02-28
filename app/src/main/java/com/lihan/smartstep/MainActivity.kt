package com.lihan.smartstep

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
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
import com.lihan.smartstep.ui.theme.BackgroundMain
import com.lihan.smartstep.ui.theme.SmartStepTheme
import kotlinx.serialization.Serializable
import org.koin.android.ext.android.inject

sealed interface Route{

    @Serializable
    data object OnboardingProfileSetting: Route

    @Serializable
    data object SmartStep: Route

    @Serializable
    data object PersonalSettings: Route

}

class MainActivity : ComponentActivity() {

    private val mainViewModel by inject<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition{
            !mainViewModel.state.value.isReady
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                BackgroundMain.toArgb(),BackgroundMain.toArgb()
            )
        )

        setContent {
            SmartStepTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = mainViewModel.state.value.startDestination
                    ){
                        composable<Route.OnboardingProfileSetting>{
                            OnboardingProfileScreenRoot(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                onNavigateToSmartStep = {
                                    navController.navigate(Route.SmartStep)
                                }
                            )
                        }
                        composable<Route.SmartStep>{
                            SmartStepScreenRoot(
                                onExit = {
                                    this@MainActivity.finish()
                                },
                                onNavigateToPersonSettings = {
                                    navController.navigate(Route.PersonalSettings)
                                }
                            )
                        }
                        composable<Route.PersonalSettings>{
                            PersonalSettingsScreenRoot(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
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
