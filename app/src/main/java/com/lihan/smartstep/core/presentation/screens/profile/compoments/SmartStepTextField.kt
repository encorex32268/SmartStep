package com.lihan.smartstep.core.presentation.screens.profile.compoments

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.bodyLargeRegular
import com.lihan.smartstep.ui.theme.bodySmallRegular

@Composable
fun SmartStepTextField(
    title: String,
    textState: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: @Composable (()->Unit)? = null,
    onClick: () -> Unit = {}
) {
    BasicTextField(
        state = textState,
        enabled = enabled,
        lineLimits = TextFieldLineLimits.SingleLine,
        decorator = { innerField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmallRegular,
                        color = TextSecondary
                    )
                    innerField()
                }
                if (trailingIcon != null){
                    trailingIcon()
                }
            }
        },
        textStyle = MaterialTheme.typography.bodyLargeRegular.copy(
            color = TextPrimary
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = {
                    //when enabled is false , will be normal row
                    if (!enabled){
                        onClick()
                    }
                }
            )
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = StrokeMain,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )

}


@Preview(showBackground = true)
@Composable
private fun SmartStepTextFieldPreview() {
    SmartStepTheme {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(24.dp)
        ){
            SmartStepTextField(
                title = "Steps",
                textState = TextFieldState(initialText = "10020"),
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrows_down),
                        contentDescription = null
                    )
                },
                enabled = true
            )
        }
    }
}