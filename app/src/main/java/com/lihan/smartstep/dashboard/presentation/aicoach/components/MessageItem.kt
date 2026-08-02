package com.lihan.smartstep.dashboard.presentation.aicoach.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.buttons.IconButtonSize
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepIconButton
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain

@Composable
fun MessageItem(
    sender: Sender,
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ){
        when(sender){
            Sender.AI -> {
                Row(
                    modifier = modifier
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SmartStepIconButton(
                        iconButtonSize = IconButtonSize.EXTRA_SMALL,
                        imageVector = AppIcons.Robot,
                        contentDescription = null,
                        containerColor = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        tintColor = BackgroundWhite
                    )
                    Surface(
                        border = BorderStroke(1.dp,StrokeMain),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp ),
                        color = BackgroundWhite,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        SelectionContainer {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = message
                            )
                        }
                    }
                }
            }
            Sender.User -> {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .widthIn(max = 275.dp),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 8.dp, bottomStart = 16.dp, bottomEnd = 16.dp ),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    SelectionContainer {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = message
                        )

                    }
                }
            }
        }

    }

}

@Composable
fun AILoadingItem(
    modifier: Modifier = Modifier,
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmartStepIconButton(
            iconButtonSize = IconButtonSize.EXTRA_SMALL,
            imageVector = AppIcons.Robot,
            contentDescription = null,
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape,
            tintColor = BackgroundWhite
        )
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 3.dp
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MessageITemPreview() {
    SmartStepTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MessageItem(
                sender = Sender.AI,
                message = "Hello! I'm your AI fitness coach. I've noticed your activity levels are a bit lower than usual today. I'm here to help you get back on track and answer any questions you might have about your fitness journey."
            )
            MessageItem(
                sender = Sender.User,
                message = "What should I do to increase my activity today?"
            )

            AILoadingItem()
        }

    }
}