package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.ui.theme.BackgroundTertiary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun WheelPicker(
    items: List<String>,
    onValueChanged: (String) -> Unit,
    itemContent: @Composable (centerIndex: Int,index: Int,value: String) -> Unit,
    modifier: Modifier = Modifier,
    initIndex: Int = 0,
    listState: LazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initIndex),
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val fixedItemHeightPx = with(density) { 44.dp.toPx() }

    var itemHeight by remember {
        mutableFloatStateOf(fixedItemHeightPx)
    }


    val flingBehavior = rememberSnapFlingBehavior(
        lazyListState = listState
    )
    val centerIndex by remember(items){
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (visibleItemsInfo.isEmpty()) return@derivedStateOf -1

            val viewportCenter =
                ((layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2f)

            visibleItemsInfo.minByOrNull { item ->
                val itemCenter = item.offset + (item.size / 2f)
                (itemCenter - viewportCenter).absoluteValue
            }?.index ?: -1
        }
    }



    val verticalPadding =  with(density){ ((176.dp.toPx() - itemHeight ) / 2).toDp() }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling){
                    if (centerIndex != -1){
                        onValueChanged(
                            items[centerIndex]
                        )
                    }
                }
            }
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(176.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(density) { itemHeight.toDp() })
                .background(BackgroundTertiary)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp),
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(
                vertical = verticalPadding
            ),
            content = {
                itemsIndexed(items) { index, itemString ->
                    Box(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = {
                                scope.launch {
                                    listState.animateScrollToItem(index)
                                }
                            }
                        )
                    ){
                        itemContent(
                            centerIndex,
                            index,
                            itemString
                        )
                    }
                }
            }
        )
    }
}



@Preview(showBackground = true)
@Composable
private fun WheelPickerPreviewPreview() {
    SmartStepTheme {
        val items = remember {
            (0..100).map { "${145+it}" }
        }
        Row {
            WheelPicker(
                modifier = Modifier.weight(1f),
                onValueChanged = {},
                items = items,
                listState = rememberLazyListState(
                    initialFirstVisibleItemIndex = items.size / 2
                ),
                itemContent = { center , index, itemString ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = itemString,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                        Text(
                            text = "Ft",
                            modifier = Modifier.alpha(
                                if (center == index) 1f else 0f
                            )
                        )
                    }
                }
            )
//            WheelPicker(
//                modifier = Modifier.weight(1f),
//                items = items,
//                listState = rememberLazyListState(
//                    initialFirstVisibleItemIndex = items.size / 2
//                ),
//                itemContent = { center , index, itemString ->
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Text(
//                            text = itemString,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier
//                        )
//                        Text(
//                            text = "Ft",
//                            modifier = Modifier.alpha(
//                                if (center == index) 1f else 0f
//                            )
//                        )
//                    }
//                }
//            )
        }
    }
}
