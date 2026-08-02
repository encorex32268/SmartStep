package com.lihan.smartstep.dashboard.presentation.aicoach.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.buttons.IconButtonSize
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepIconButton
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain
import com.lihan.smartstep.dashboard.presentation.aicoach.quickSuggestions

@Composable
fun MessageSendBar(
    textFieldState: TextFieldState,
    isShowSuggestions: Boolean,
    onSend: () ->Unit,
    onShowSuggestions: () ->Unit,
    onSelectedSuggestion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onShowSuggestions, interactionSource = null),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.quick_suggestions),
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (isShowSuggestions) AppIcons.ArrowUp else AppIcons.ArrowDown,
                    contentDescription = null
                )
            }
            AnimatedVisibility(isShowSuggestions) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        quickSuggestions.forEach { textResId ->
                            val suggestion = stringResource(textResId)
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onBackground,
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    onSelectedSuggestion(suggestion)
                                },
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = StrokeMain
                                ),

                                ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 16.dp),
                                ){
                                    Text(
                                        text = suggestion,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                state = textFieldState,
                decorator = { innerField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ){
                        if (textFieldState.text.isEmpty()){
                            Text(
                                text = stringResource(R.string.ai_ask_place_holder),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                        innerField()
                    }
                },
                interactionSource = interactionSource,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = if (isFocused){
                            MaterialTheme.colorScheme.primary
                        }else{
                            StrokeMain
                        }
                    )
                    .padding(16.dp)
            )
            SmartStepIconButton(
                imageVector = AppIcons.SendMessage,
                contentDescription = null,
                onClick = onSend,
                iconButtonSize = IconButtonSize.MEDIUM,
                containerColor = MaterialTheme.colorScheme.primary,
                tintColor = BackgroundWhite,
                shape = CircleShape
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun MessageSendBarPreview() {
    SmartStepTheme {
        Box(
            modifier = Modifier.padding(24.dp)
        ){

            MessageSendBar(
                textFieldState = TextFieldState(),
                onSend = {},
                onSelectedSuggestion = {},
                onShowSuggestions = {},
                isShowSuggestions = true
            )
        }
    }
}