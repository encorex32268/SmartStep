@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.stepcount.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.AdaptiveModal
import com.lihan.smartstep.core.presentation.components.ModalListPicker
import com.lihan.smartstep.core.presentation.components.WheelPicker
import com.lihan.smartstep.core.presentation.design_system.PrimaryButton
import com.lihan.smartstep.core.presentation.design_system.SmartStepTextButton
import com.lihan.smartstep.stepcount.presentation.components.StepsCard
import com.lihan.smartstep.ui.theme.BackgroundMain
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.BackgroundWhite20
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.bodyLargeMedium
import kotlinx.coroutines.launch

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
)


@Composable
fun SmartStepScreenRoot(
    onExit: () -> Unit,
    onNavigateToPersonSettings: () -> Unit,
    viewModel: SmartStepViewModel = viewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    SmartStepScreen(
        drawerState = drawerState,
        state = state,
        onAction = { action ->
            if (action in closeDrawerActions){
                scope.launch { drawerState.close() }
            }
            when(action){
                SmartStepAction.OnMenuClick -> {
                    scope.launch {
                        drawerState.open()
                    }
                }
                SmartStepAction.OnExitClick -> {
                    onExit()
                }
                SmartStepAction.OnPersonSettingsClick -> {
                    onNavigateToPersonSettings()
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}


@Composable
fun SmartStepScreen(
    drawerState: DrawerState,
    state: SmartStepState,
    onAction: (SmartStepAction) -> Unit,
    modifier: Modifier = Modifier
) {

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                drawerContainerColor = BackgroundSecondary,
                windowInsets = WindowInsets.statusBars
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    itemsIndexed(drawerItems){ index , drawerItem ->
                        if(index != 0){
                            HorizontalDivider(color = StrokeMain)
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable{
                                    when(drawerItem.id){
                                        R.string.step_goal -> onAction(SmartStepAction.OnStepGoalClick)
                                        R.string.personal_settings -> onAction(SmartStepAction.OnPersonSettingsClick)
                                        R.string.exit -> onAction(SmartStepAction.OnExitClick)
                                    }
                                }
                                .padding(vertical = 16.dp, horizontal = 24.dp),
                            text = stringResource(drawerItem.id),
                            style = MaterialTheme.typography.bodyLargeMedium,
                            color = drawerItem.textColor
                        )

                    }
                }

            }
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundMain)
        ) {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundMain
                ),
                windowInsets =  WindowInsets.systemBars,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(SmartStepAction.OnMenuClick)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.menu),
                            contentDescription = "Menu",
                            tint = TextPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.smart_step_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }
            )
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ){
                StepsCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    steps = state.step.formatThousands(),
                    stepsTotal = state.totalStep.toString()
                )

            }
        }

    }

    if (state.isShowStepGoal){
        //TODO: Show StepGoal
        AdaptiveModal(
            onDismiss = {
                onAction(SmartStepAction.OnDismissStepGoal)
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 312.dp)
                        .heightIn(max = 376.dp)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.modal_step_goal),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(16.dp))
                    WheelPicker(
                        items = stepGoalItems.reversed(),
                        initIndex = stepGoalItems.lastIndex - 2,
                        onValueChanged = { string ->
                            onAction(SmartStepAction.OnStepGoalValueChanged(string))
                        },
                        itemContent = { centerIndex, index , itemString ->
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .padding(vertical = 10.dp),
                                text = itemString,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (centerIndex  != index){
                                    TextSecondary
                                }else{
                                    TextPrimary
                                },
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                    Spacer(Modifier.height(24.dp))
                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.save),
                        onClick = {
                            onAction(SmartStepAction.OnStepGoalSaveClick)
                        }
                    )
                    SmartStepTextButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.cancel),
                        onClick = {
                            onAction(SmartStepAction.OnDismissStepGoal)
                        }
                    )

                }
            }
        )
    }



}


@Preview(showBackground = true)
@Composable
private fun SmartStepScreenPreview() {
    SmartStepTheme {
        SmartStepScreen(
            state = SmartStepState(
                step = 1234,
                isShowStepGoal = false
            ),
            onAction = {},
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        )
    }
}