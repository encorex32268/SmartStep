package com.lihan.smartstep.core.presentation.modifier

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.negativePadding(horizontal: Dp): Modifier {
    return this.then(NegativePaddingElement(horizontal))
}

@SuppressLint("ModifierNodeInspectableProperties")
private data class NegativePaddingElement(
    private val horizontal: Dp = 0.dp
): ModifierNodeElement<NegativePaddingNode>(){
    override fun create(): NegativePaddingNode {
        return NegativePaddingNode(horizontal)
    }

    override fun update(node: NegativePaddingNode) {
       node.horizontal = horizontal
    }
}


private class NegativePaddingNode(
    var horizontal: Dp = 0.dp
): LayoutModifierNode, Modifier.Node(){

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {

        val px = horizontal.roundToPx()

        val placeable = measurable.measure(
            constraints = constraints.copy(
                minWidth = constraints.minWidth + 2 * px,
                maxWidth = constraints.maxWidth + 2 * px
            )
        )
        return layout(placeable.width,placeable.height){
            placeable.place(0,0)
        }

    }

}















