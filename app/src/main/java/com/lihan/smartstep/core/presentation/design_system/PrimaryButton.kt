package com.lihan.smartstep.core.presentation.design_system

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextWhite
import com.lihan.smartstep.ui.theme.bodyLargeMedium

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonColors(
            containerColor = ButtonPrimary,
            contentColor = TextWhite,
            disabledContentColor = StrokeMain,
            disabledContainerColor = Color.Transparent
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLargeMedium,
            color = TextWhite
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPreview() {
    SmartStepTheme {
        PrimaryButton(
            text = "Button",
            onClick = {}
        )
    }
}