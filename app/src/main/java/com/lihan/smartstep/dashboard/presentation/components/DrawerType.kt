package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lihan.smartstep.R

enum class DrawerType {
    FixIssue,StepGoal,PersonalSettings,Exit
}

@Composable
fun DrawerType.toStringResource(): String {
    return when(this){
        DrawerType.FixIssue -> stringResource(R.string.fix_stop_counting_steps_issue)
        DrawerType.StepGoal -> stringResource(R.string.step_goal)
        DrawerType.PersonalSettings -> stringResource(R.string.personal_settings)
        DrawerType.Exit -> stringResource(R.string.exit)
    }
}