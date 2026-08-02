package com.lihan.smartstep.dashboard.presentation.aicoach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.topbar.SmartStepTopbar
import com.lihan.smartstep.core.presentation.ui.theme.AICoachBackIcon
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain
import com.lihan.smartstep.dashboard.domain.Message
import com.lihan.smartstep.dashboard.presentation.aicoach.components.AILoadingItem
import com.lihan.smartstep.dashboard.presentation.aicoach.components.MessageItem
import com.lihan.smartstep.dashboard.presentation.aicoach.components.MessageSendBar
import com.lihan.smartstep.dashboard.presentation.aicoach.components.Sender
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AICoachRoot(
    onBack: () -> Unit,
    viewModel: AICoachViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    //Greeting from ai
    val greeting = stringResource(R.string.ai_greeting)
    LaunchedEffect(Unit) {
        viewModel.onAction(AICoachAction.OnGreetingFromAI(greeting))
    }

    AICoachScreen(
        state = state,
        onAction = { action ->
            when(action){
                AICoachAction.OnBackClick -> onBack()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun AICoachScreen(
    state: AICoachState,
    onAction: (AICoachAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size, state.isThinking) {
        if (state.messages.isNotEmpty()) {
            val targetIndex = state.messages.size - 1 + if (state.isThinking) 1 else 0
            listState.animateScrollToItem(index = targetIndex)
        }
    }

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
                HorizontalDivider(color = StrokeMain)

            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(color = BackgroundWhite)
                    .navigationBarsPadding()
            ){
                HorizontalDivider(color = StrokeMain)
                MessageSendBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textFieldState = TextFieldState(),
                    onSend = {
                        onAction(AICoachAction.OnSendClick)
                    },
                    onSelectedSuggestion = { suggestion ->
                        onAction(AICoachAction.OnSelectedSuggestion(suggestion))
                    },
                    isShowSuggestions = state.isShowSuggestions,
                    onShowSuggestions = {
                        onAction(AICoachAction.OnSuggestionClick)
                    }
                )

            }
        }
    ) { paddingValues ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.messages){ message ->
                MessageItem(
                    modifier = Modifier.fillMaxWidth(),
                    sender = message.sender,
                    message  = message.message
                )
            }
            if (state.isThinking){
                item {
                    AILoadingItem()
                }
            }
        }

    }

}

@Preview
@Composable
private fun Preview() {
    SmartStepTheme {
        AICoachScreen(
            state = AICoachState(
                messages = (0..10).map {
                    Message(
                        sender = if (it % 2 == 0) Sender.AI else Sender.User,
                        message = "Message - $it"
                    )
                }
            ),
            onAction = {}
        )
    }
}