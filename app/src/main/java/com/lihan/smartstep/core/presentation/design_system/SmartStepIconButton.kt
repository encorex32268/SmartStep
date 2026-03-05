package com.lihan.smartstep.core.presentation.design_system

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.ui.theme.BackgroundWhite20
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextWhite

@Composable
fun SmartStepIconButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    tintColor: Color = TextWhite,
    contentDescription: String?=null,
    onClick: (() -> Unit)?=null,
    containerColor: Color = BackgroundWhite20,
    enabled: Boolean = true,
) {
    IconButton(
        modifier = modifier,
        onClick = {
            if (onClick!=null && enabled){
                onClick()
            }
        },
        enabled = enabled,
        shape = shape,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor
        )
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tintColor
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun SmartStepIconButtonPreview() {
    SmartStepTheme {
        SmartStepIconButton(
            onClick = {},
            imageVector = Sneaker,
            containerColor = ButtonPrimary
        )
    }
}