package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.design_system.SmartStepIconButton
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.ButtonPrimary
import com.lihan.smartstep.core.presentation.ui.theme.ButtonSecondary
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.TextPrimary
import com.lihan.smartstep.core.presentation.ui.theme.bodyLargeMedium
import com.lihan.smartstep.core.presentation.ui.theme.regular

@Composable
fun AICoachCard(
    isConnecting: Boolean,
    message: String,
    onTryAgainClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp)
) {
    Column(
        modifier = modifier
            .clip(shape)
            .background(color = BackgroundWhite, shape = shape)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmartStepIconButton(
                imageVector = ImageVector.vectorResource(R.drawable.ai_artificial_intelligence),
                containerColor = ButtonSecondary,
                tintColor = ButtonPrimary
            )
            Spacer(Modifier.weight(1f))
            if (!isConnecting){
                Row(
                    modifier = Modifier.clickable{ onTryAgainClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.try_again),
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = ButtonPrimary
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.refresh),
                        contentDescription = stringResource(R.string.try_again_refresh),
                        tint = ButtonPrimary
                    )
                }
            }else{
                Row(
                    modifier = Modifier.clickable{ onMoreClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.more),
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = ButtonPrimary
                    )
                    Icon(
                       imageVector = ImageVector.vectorResource(R.drawable.arrows_right),
                        contentDescription = stringResource(R.string.more_arrow_right),
                        tint = ButtonPrimary
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.regular.copy(
                color = TextPrimary,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        )

    }
}

@Preview
@Composable
private fun AICoachCardPreview() {
    SmartStepTheme {
        val isConnecting = remember { true }
        AICoachCard(
            isConnecting = isConnecting,
            onTryAgainClick = {},
            onMoreClick =  {},
            message = if (isConnecting){
                "You are slight ly behind today ’s pace — 1.2k steps needed."
            }else{
                stringResource(R.string.ai_coach_disconnecting)
            }
        )
    }
}