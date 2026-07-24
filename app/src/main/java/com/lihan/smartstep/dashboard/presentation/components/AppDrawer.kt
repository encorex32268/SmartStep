package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme

@Composable
fun AppDrawer(
    items: List<DrawerType>,
    drawerState: DrawerState,
    onItemClick: (DrawerType) -> Unit,
    content: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    ModalNavigationDrawer(
        modifier = modifier,
        gesturesEnabled = true,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.85f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    items.forEachIndexed { index, type ->
                        if (index != 0){
                            HorizontalDivider()
                        }
                        if (type == DrawerType.Exit){
                            DrawerItem(
                                text = type.toStringResource() ,
                                textColor = MaterialTheme.colorScheme.primary,
                                onItemClick = { onItemClick(type) }
                            )
                        }else{
                            DrawerItem(
                                text = type.toStringResource(),
                                onItemClick = { onItemClick(type) }
                            )
                        }

                    }
                }
            }
        },
        content = content
    )
}




@Composable
private fun DrawerItem(
    text: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onBackground
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onItemClick
            )
            .padding(vertical = 16.dp, horizontal = 24.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun AppDrawerPreview() {
    SmartStepTheme {
        AppDrawer(
            items = DrawerType.entries,
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            onItemClick = {},
            content = {}
        )
    }
}