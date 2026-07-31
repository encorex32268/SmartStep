package com.lihan.smartstep.dashboard.presentation.aicoach

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.topbar.SmartStepTopbar
import com.lihan.smartstep.core.presentation.ui.theme.AICoachBackIcon
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AICoachRoot(
    viewModel: AICoachViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AICoachScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AICoachScreen(
    state: AICoachState,
    onAction: (AICoachAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                SmartStepTopbar(
                    title = stringResource(R.string.ai_coach),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onAction(AICoachAction.OnBackClick)
                            }
                        ) {
                            Icon(
                                imageVector = AppIcons.ArrowLeft,
                                contentDescription = null,
                                tint = AICoachBackIcon
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                HorizontalDivider(color = Color.Red)
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ){
            Text(
                text = "AI Coach"
            )
        }

    }

}

@Preview
@Composable
private fun Preview() {
    SmartStepTheme {
        AICoachScreen(
            state = AICoachState(),
            onAction = {}
        )
    }
}