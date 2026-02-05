package com.lihan.smartstep.core.presentation.design_system

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.bodyLargeMedium

@Composable
fun SmartStepTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = ButtonPrimary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLargeMedium,
            color = ButtonPrimary
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun SmartStepTextButtonPreview() {
    SmartStepTheme {
        SmartStepTextButton(
            text = "Cancel",
            onClick = {}
        )
    }
}