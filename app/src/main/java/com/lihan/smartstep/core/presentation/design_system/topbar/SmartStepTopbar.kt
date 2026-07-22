@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.core.presentation.design_system.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.design_system.buttons.ButtonType
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.profile_setup.presentation.ProfileSetupAction

@Composable
fun SmartStepTopbar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors
    )

}

@Preview(showBackground = true)
@Composable
private fun SmartStepTopbarPreview() {
    SmartStepTheme {
        SmartStepTopbar(
            title = "Topbar"
        )
    }
}