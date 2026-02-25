package com.lihan.smartstep.stepcount.presentation.drawer

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lihan.smartstep.R
import com.lihan.smartstep.stepcount.presentation.SmartStepAction
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.TextPrimary

data class DrawerItem(
    @param:StringRes val id: Int,
    val textColor: Color
)

val drawerItems = listOf(
    DrawerItem(R.string.step_goal , TextPrimary),
    DrawerItem(R.string.personal_settings , TextPrimary),
    DrawerItem(R.string.exit , ButtonPrimary),
)
val closeDrawerActions = listOf(
    SmartStepAction.OnExitClick,
    SmartStepAction.OnPersonSettingsClick,
    SmartStepAction.OnStepGoalClick,
    SmartStepAction.OnShowEnableAccessModal
)