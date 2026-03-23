@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.coach.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.smartstep.R
import com.lihan.smartstep.coach.presentation.components.MessageCard
import com.lihan.smartstep.coach.presentation.components.SuggestionsField
import com.lihan.smartstep.coach.presentation.model.MessageUi
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundMain
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundSecondary
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain
import com.lihan.smartstep.core.presentation.ui.theme.TextPrimary
import com.lihan.smartstep.core.presentation.ui.theme.medium
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun AICoachScreenRoot(
    viewModel: AICoachViewModel = koinViewModel(),
    onBack: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
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
private fun AICoachScreen(
    state: AICoachState,
    onAction: (AICoachAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val suggestions = stringArrayResource(R.array.suggestions).toList()
    val listState = rememberLazyListState()

    LaunchedEffect(state.items) {
        if (state.items.isNotEmpty()){
            //auto scroll to bottom
            listState.animateScrollToItem(
                state.items.size - 1
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundMain)
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BackgroundSecondary
            ),
            title = {
                Text(
                    text = stringResource(R.string.ai_coach),
                    style = MaterialTheme.typography.medium,
                    color = TextPrimary
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onAction(AICoachAction.OnBackClick)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_left),
                        contentDescription = stringResource(R.string.ai_coach_arrow_left)
                    )
                }
            }
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = StrokeMain,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = BackgroundSecondary)
                .weight(1f)
                .padding(16.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.items){ messageUi ->
                MessageCard(
                    modifier = Modifier.fillMaxWidth(),
                    isUser = messageUi.isUser,
                    message = messageUi.content
                )
            }
            if (state.isAiThinking){
                item {
                    MessageCard(
                        modifier = Modifier.fillMaxWidth(),
                        isUser = false,
                        message = "....."
                    )
                }
            }

        }
        SuggestionsField(
            isExpandSuggestions = state.isExpandSuggestion,
            textFieldState = state.suggestionTextField,
            enabled = state.isTextFieldEnabled,
            suggestions = suggestions,
            onSuggestionClick = { suggestion ->
                onAction(AICoachAction.OnSuggestionClick(suggestion))
            },
            onSendClick = {
                onAction(AICoachAction.OnSendClick)
            },
            onQuickSuggestionClick = {
                onAction(AICoachAction.OnQuickSuggestionClick)
            }
        )




    }

}

@Preview
@Composable
private fun AICoachScreenPreview() {
    SmartStepTheme {
        AICoachScreen(
            state = AICoachState(
                items = (0..5).map {
                    MessageUi(
                        isUser = Random.nextBoolean(),
                        content = "Content - $it"
                    )
                }
            ),
            modifier = Modifier.fillMaxSize(),
            onAction = {

            }
        )
    }
}