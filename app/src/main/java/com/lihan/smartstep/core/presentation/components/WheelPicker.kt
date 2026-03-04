package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.stepcount.presentation.components.withZero
import com.lihan.smartstep.ui.theme.BackgroundTertiary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@Composable
fun WheelPicker(
    items: List<String>,
    value: String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
){

    val itemHeight = 44.dp
    val pagerState = rememberPagerState(
        initialPage = if (items.isEmpty()) 0 else items.indexOf(value),
        pageCount = { items.size } )
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onValueChange(pagerState.currentPage)
    }

    VerticalPager(
        state = pagerState,
        pageSize = PageSize.Fixed(itemHeight),
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight * 4),
        contentPadding = PaddingValues(vertical = itemHeight * 2)
    ) { index ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .clickable(
                    indication = null,
                    interactionSource = null,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = items[index].withZero(),
                style = MaterialTheme.typography.titleMedium,
                color = if (index == pagerState.currentPage) TextPrimary else TextSecondary,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun WheelPicker2Preview() {
    SmartStepTheme {
        Row(
            modifier = Modifier.fillMaxWidth().height(176.dp)
        ){
            WheelPicker(
                modifier = Modifier.weight(1f),
                items = (0..9).map{ "$it" },
                value = "5",
                onValueChange = {

                }
            )
        }
    }

}
