package com.lihan.smartstep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lihan.smartstep.onboarding.presentation.OnboardingProfileScreenRoot
import com.lihan.smartstep.stepcount.data.local.SmartStepDatabase
import com.lihan.smartstep.stepcount.data.mapper.toEntity
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import com.lihan.smartstep.stepcount.presentation.SmartStepScreenRoot
import com.lihan.smartstep.stepcount.presentation.personalsettings.PersonalSettingsScreenRoot
import com.lihan.smartstep.ui.theme.BackgroundMain
import com.lihan.smartstep.ui.theme.SmartStepTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.ZoneId

sealed interface Route{

    @Serializable
    data object OnboardingProfileSetting: Route

    @Serializable
    data object SmartStep: Route

    @Serializable
    data object PersonalSettings: Route

    @Serializable
    data object Test: Route

}

class MainActivity : ComponentActivity() {

    private val mainViewModel by inject<MainViewModel>()
    private val smartStepDatabase by inject<SmartStepDatabase>()


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

                            composable<Route.Test>{
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Button(
                                        onClick = {
                                            lifecycleScope.launch {
                                                val startDay = LocalDateTime.of(2026,3,1,0,0)
                                                (0..11).map { day ->
                                                    val goal = (day+1) *1000L
                                                    smartStepDatabase.dailyStepDao.upsert(
                                                        DailyStep(
                                                            goal = goal,
                                                            steps = goal - 500,
                                                            time = 10_000,
                                                            dayTimestamp = startDay
                                                                .plusDays(day.toLong())
                                                                .atZone(ZoneId.systemDefault())
                                                                .toInstant()
                                                                .toEpochMilli()
                                                        ).toEntity()
                                                    )
                                                }

                                            }


                                        }
                                    ) {
                                        Text(
                                            text = "Create Mock Data"
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
























