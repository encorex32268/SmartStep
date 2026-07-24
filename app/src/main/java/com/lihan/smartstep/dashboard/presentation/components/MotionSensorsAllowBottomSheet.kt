@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.bottomsheet.SmartStepModalBottomSheet
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain


@Composable
fun MotionSensorsAllowBottomSheet(
    onAllowAccess: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(8.dp),
            color = BackgroundWhite,
            border = BorderStroke(1.dp, StrokeMain)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = AppIcons.LocationDirection,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.motion_sensors_allow_description),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )
        Spacer(Modifier.height(44.dp))
        SmartStepButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.allow_access),
            onClick = onAllowAccess
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun MotionSensorsAllowBottomSheetRootPreview() {
    SmartStepTheme {
        MotionSensorsAllowBottomSheet(
            onAllowAccess = {}
        )
    }
}