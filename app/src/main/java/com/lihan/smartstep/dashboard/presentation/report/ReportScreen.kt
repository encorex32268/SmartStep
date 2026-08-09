package com.lihan.smartstep.dashboard.presentation.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ReportRoot(
    viewModel: ReportViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ReportScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ReportScreen(
    state: ReportState,
    onAction: (ReportAction) -> Unit,
    modifier: Modifier = Modifier
) {

}

@Preview(showBackground = true)
@Composable
private fun ReportScreenPreview() {
    ReportScreen(
        state = ReportState(),
        onAction = {}
    )
}