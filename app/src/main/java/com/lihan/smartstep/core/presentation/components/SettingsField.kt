package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.data.model.Gender
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import org.koin.dsl.module

@Composable
fun SettingsTextField(
    title: String,
    value: TextFieldState,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                BasicTextField(
                    state = value,
                    decorator = { innerField ->
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ){
                            innerField()
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = keyboardOptions,
                    onKeyboardAction = KeyboardActionHandler { onDone() },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    inputTransformation = InputTransformation.maxLength(6)
                )
            }
        }
    }
}


@Composable
fun SettingsField(
    title: String,
    value: String,
    onFieldClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = AppIcons.ArrowDown,
    shape: Shape = RoundedCornerShape(10.dp)
){
    Row(
        modifier = modifier
            .clip(shape)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape =shape
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            )
            .clickable(
                onClick = onFieldClick
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (trailingIcon != null){
            Icon(
                imageVector = trailingIcon,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
    }

}






@Composable
fun SettingsDropdown(
    title: String,
    value: String,
    isDropdown: Boolean,
    dropDownItems: List<String>,
    onDropdownClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp)
) {
    val density = LocalDensity.current

    var fieldWidth by rememberSaveable {
        mutableIntStateOf(0)
    }

    Box(
        modifier = modifier
    ){
        SettingsField(
            title = title,
            value = value,
            onFieldClick = onDropdownClick,
            modifier = Modifier.onGloballyPositioned{ layoutCoordinates ->
                fieldWidth = layoutCoordinates.size.width
            }
        )
        DropdownMenu(
            expanded = isDropdown,
            shape = RoundedCornerShape(8.dp),
            onDismissRequest = onDismissRequest,
            modifier = Modifier.width(with(density){fieldWidth.toDp()}),
            containerColor = Color.White,
            offset = DpOffset(x = 0.dp , y = 8.dp),
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                ) {
                    dropDownItems.forEach { dropdownItem ->
                        val isSelected = dropdownItem == value

                        DropdownMenuItem(
                            modifier = Modifier
                                .clip(shape)
                                .background(
                                    color = if (isSelected){
                                        MaterialTheme.colorScheme.surface
                                    }else{
                                        Color.Transparent
                                    },
                                    shape = shape
                                ),
                            text = {
                                Text(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    text = dropdownItem,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                            },
                            onClick = {
                                onItemClick(dropdownItem)
                            },
                            trailingIcon = {
                                if (isSelected){
                                    Icon(
                                        imageVector = AppIcons.Check,
                                        contentDescription = null
                                    )
                                }
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = MaterialTheme.colorScheme.onSurface,
                                trailingIconColor = MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 6.dp
                            )
                        )

                    }
                }
            }
        )

    }




}


@Preview(showBackground = true)
@Composable
private fun SettingsDropdownPreview() {
    SmartStepTheme {

        var value by remember { mutableStateOf(Gender.Female) }
        var isDropdown by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            SettingsField(
                title = "Height",
                value = "170 cm",
                onFieldClick = {}
            )

            SettingsTextField(
                title = "Steps",
                value = TextFieldState(initialText = "1000"),
                onDone = {}
            )


            SettingsDropdown(
                title = "Gender",
                value = value.name,
                isDropdown = isDropdown,
                dropDownItems = Gender.entries.map { it.name },
                onDismissRequest = {
                    isDropdown = false
                },
                onDropdownClick = {
                    isDropdown = !isDropdown
                },
                onItemClick = {
                    value = Gender.fromName(it)
                    isDropdown = false
                }
            )

        }
    }
}