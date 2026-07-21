package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleValueWheelPicker(
    value: String,
    items: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 44.dp,
    visibleCount: Int = 4
) {

    val initialIndex = remember(items, value) {
        val index = items.indexOf(value)
        if (index == -1) {
            if (items.isEmpty()) 0 else items.size / 2
        } else {
            index
        }
    }

    val pagerState = androidx.compose.runtime.key(items) {
        rememberPagerState(
            initialPage = initialIndex,
            pageCount = { items.size }
        )
    }
    val scope = rememberCoroutineScope()


    val pagerHeight by remember(visibleCount,itemHeight) {
        mutableStateOf(itemHeight * visibleCount)
    }

    LaunchedEffect(value) {
        val targetIndex = items.indexOf(value)
        if (targetIndex != -1 && targetIndex != pagerState.currentPage) {
            pagerState.animateScrollToPage(targetIndex)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { isInProgress ->
                if (!isInProgress) {
                    val returnValue = items.getOrNull(pagerState.currentPage)?:return@collect
                    onValueChange(returnValue)
                }
            }
    }

    VerticalPager(
        state = pagerState,
        pageSize = PageSize.Fixed(itemHeight),
        modifier = modifier
            .fillMaxWidth()
            .height(pagerHeight),
        contentPadding = PaddingValues(
            vertical = itemHeight * 2
        )
    ) { index ->
        val item = items.getOrNull(index) ?: ""
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item,
                style = MaterialTheme.typography.titleMedium,
                color = if (index == pagerState.currentPage)
                    MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleValueWheelPickerPreview() {
    SmartStepTheme {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ){
            SingleValueWheelPicker(
                items = (0..100).map { it.toString() },
                onValueChange = {},
                value = "50"
            )
        }
    }
}


