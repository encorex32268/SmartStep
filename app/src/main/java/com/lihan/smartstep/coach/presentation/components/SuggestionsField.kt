package com.lihan.smartstep.coach.presentation.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundSecondary
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.ButtonPrimary
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain
import com.lihan.smartstep.core.presentation.ui.theme.TextPrimary
import com.lihan.smartstep.core.presentation.ui.theme.TextSecondary
import com.lihan.smartstep.core.presentation.ui.theme.medium
import com.lihan.smartstep.core.presentation.ui.theme.regular
import com.lihan.smartstep.core.presentation.util.negativePadding

@Composable
fun SuggestionsField(
    isExpandSuggestions: Boolean,
    textFieldState: TextFieldState,
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    onSendClick: () -> Unit,
    onQuickSuggestionClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocus by interactionSource.collectIsFocusedAsState()

    val borderColor = remember(isFocus,enabled) {
        when{
            (!enabled) || (!isFocus) ->  StrokeMain
            else -> ButtonPrimary
        }
    }


    Column(
        modifier = modifier
            .background(BackgroundWhite)
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = StrokeMain,
            modifier = Modifier.negativePadding(16.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = onQuickSuggestionClick
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.ai_coach_quick_suggestions),
                    style = MaterialTheme.typography.medium,
                    color = TextPrimary
                )
                Icon(
                    imageVector = if (isExpandSuggestions){
                        ImageVector.vectorResource(R.drawable.arrows_up)
                    }else{
                        ImageVector.vectorResource(R.drawable.arrows_down)
                    },
                    contentDescription =null
                )

            }

            AnimatedVisibility(visible = isExpandSuggestions) {
                Column{
                    Spacer(Modifier.height(16.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestions.forEach { suggestion ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp,StrokeMain,RoundedCornerShape(10.dp))
                                    .background(BackgroundSecondary,RoundedCornerShape(10.dp))
                                    .clickable{
                                        onSuggestionClick(suggestion)
                                    }
                                    .padding(vertical = 12.dp, horizontal = 16.dp)
                            ){
                                Text(
                                    text = suggestion,
                                    style = MaterialTheme.typography.medium.copy(
                                        fontSize = 16.sp,
                                        color = TextPrimary
                                    )
                                )
                            }
                        }

                    }
                }
            }

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                state = textFieldState,
                enabled = enabled,
                interactionSource = interactionSource,
                decorator = { innerField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ){
                        if (textFieldState.text.toString().isEmpty()){
                            Text(
                                text = stringResource(R.string.ask_me_anything),
                                style = MaterialTheme.typography.regular.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                        innerField()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = BackgroundWhite,
                        shape =  RoundedCornerShape(10.dp)
                    )
                    .padding(16.dp)
            )
            IconButton(
                onClick = onSendClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = ButtonPrimary
                )
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.send),
                    contentDescription = stringResource(R.string.suggestion_send),
                    tint = BackgroundWhite
                )
            }

        }
        Spacer(Modifier.height(16.dp))
    }

}

@Preview
@Composable
private fun SuggestionsFieldPreview() {
    SmartStepTheme {
        SuggestionsField(
            isExpandSuggestions = false,
            textFieldState = TextFieldState(),
            suggestions = listOf(
                "This is first suggestion",
                "This is second suggestion",
                "This is third suggestion",
            ),
            onSuggestionClick = {},
            onSendClick = {},
            onQuickSuggestionClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}