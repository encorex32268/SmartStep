package com.lihan.smartstep.core.presentation.design_system.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme

enum class IconButtonSize(val size: Dp){
    EXTRA_SMALL(32.dp),SMALL(38.dp),MEDIUM(44.dp)
}

@Composable
fun SmartStepIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    iconButtonSize: IconButtonSize = IconButtonSize.SMALL,
    onClick: (() -> Unit)?=null,
    shape: Shape = RoundedCornerShape(8.dp),
    tintColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
) {
    IconButton(
        modifier = modifier.size(iconButtonSize.size),
        onClick = {
            onClick?.invoke()
        },
        shape = shape,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor
        )
    ){
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tintColor
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF)
@Composable
private fun SmartStepIconButtonPreview() {
    SmartStepTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SmartStepIconButton(
                onClick = {},
                imageVector = AppIcons.Sneakers,
                contentDescription = null,
                modifier = Modifier.size(44.dp)
            )
            SmartStepIconButton(
                onClick = {},
                imageVector = AppIcons.Edit,
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(100)
            )
        }
    }
}