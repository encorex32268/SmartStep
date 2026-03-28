package com.lihan.smartstep

import android.os.Bundle
import android.text.format.DateUtils
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lihan.smartstep.coach.presentation.AICoachScreenRoot
import com.lihan.smartstep.core.data.datastore.local.SmartStepDatabase
import com.lihan.smartstep.core.domain.Route
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundMain
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.onboarding.presentation.OnboardingProfileScreenRoot
import com.lihan.smartstep.stepcount.presentation.SmartStepScreenRoot
import com.lihan.smartstep.stepcount.presentation.personalsettings.PersonalSettingsScreenRoot
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId


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

                val mainState by mainViewModel.state.collectAsStateWithLifecycle()
                val startDestination = mainState.startDestination
                val navController = rememberNavController()
                if (mainState.isReady){
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        NavHost(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            startDestination = startDestination
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
                                        this@MainActivity.finishAndRemoveTask()
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

                            composable<Route.AICoach>{
                                AICoachScreenRoot(
                                    onBack = {}
                                )
                            }

                            composable<Route.Test>{
                                val scope = rememberCoroutineScope()
                                val context = LocalContext.current
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Button(
                                        onClick = {
                                            scope.launch{
                                                withContext(Dispatchers.IO){
                                                    val file = File(
                                                        context.filesDir,
                                                        "logger.txt"
                                                    )
                                                    val now = LocalDateTime.now(ZoneId.systemDefault())
                                                    file.appendText("$now"+"\n")
                                                }
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = "Record!"
                                        )
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }
    }
}
























