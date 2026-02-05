package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.design_system.CheckMark
import com.lihan.smartstep.ui.theme.ButtonSecondary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.bodyMediumMedium

@Composable
fun Switcher(
    selectedOption: String,
    firstOption: String,
    secondOption: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .border(
                width = 1.dp,
                color = StrokeMain,
                shape = RoundedCornerShape(100)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SwitcherItem(
            modifier = Modifier
                .weight(1f)
                .clickable{
                onClick(firstOption)
            },
            text = firstOption,
            isSelected = selectedOption == firstOption,
            shape = RoundedCornerShape(
                topStart = 100.dp,
                bottomStart = 100.dp
            )
        )

        SwitcherItem(
            modifier = Modifier
                .weight(1f)
                .clickable{
                onClick(secondOption)
            },
            text = secondOption,
            isSelected = selectedOption == secondOption,
            shape = RoundedCornerShape(
                topEnd = 100.dp,
                bottomEnd = 100.dp
            )
        )
    }


}


@Composable
private fun SwitcherItem(
    text: String,
    shape: Shape,
    isSelected: Boolean,
    modifier: Modifier = Modifier
){
    val backgroundColor = remember(isSelected) {
        if (isSelected) ButtonSecondary else Color.Transparent
    }
    Row(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor,shape)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isSelected){
            Icon(
                imageVector = CheckMark,
                tint = TextPrimary,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMediumMedium,
            color = TextPrimary
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun SwitcherPreview() {
    SmartStepTheme {
        Switcher(
            selectedOption = "cm",
            firstOption = "cm",
            secondOption = "ft/in",
            onClick = {}
        )
    }
}