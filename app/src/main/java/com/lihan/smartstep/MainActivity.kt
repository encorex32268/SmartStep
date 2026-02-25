package com.lihan.smartstep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lihan.smartstep.core.data.AppUserInfo
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.onboarding.presentation.ProfileScreenRoot
import com.lihan.smartstep.onboarding.presentation.ProfileViewModel
import com.lihan.smartstep.stepcount.presentation.SmartStepScreenRoot
import com.lihan.smartstep.ui.theme.SmartStepTheme
import kotlinx.serialization.Serializable

sealed interface Route{

    @Serializable
    data object ProfileSettings: Route
    @Serializable
    data object SmartStep: Route

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
                        composable<Route.ProfileSettings>{
                            val viewModel = viewModel {
                                ProfileViewModel(
                                    userInfoDataStore = userInfoDataStore
                                )
                            }
                            ProfileScreenRoot(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize().padding(innerPadding)
                            )
                        }
                        composable<Route.SmartStep>{
                            SmartStepScreenRoot(
                                onExit = {},
                                onNavigateToPersonSettings = {
                                    navController.navigate(Route.ProfileSettings)
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartStepTheme {
        Greeting("Android")
    }
}