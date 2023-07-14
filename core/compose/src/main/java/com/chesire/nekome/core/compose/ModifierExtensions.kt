@file:OptIn(ExperimentalComposeUiApi::class)

package com.chesire.nekome.core.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.unit.Dp

/**
 * Provides autofill to a view.
 * This should be removed once compose completely supports autofill nicely.
 */
fun Modifier.autofill(
    autofillTypes: List<AutofillType>,
    onFill: ((String) -> Unit)
) = composed {
    val autofill = LocalAutofill.current
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)
    LocalAutofillTree.current += autofillNode

    this
        .onGloballyPositioned { autofillNode.boundingBox = it.boundsInWindow() }
        .onFocusChanged { focusState ->
            autofill?.run {
                if (focusState.isFocused) {
                    requestAutofillForNode(autofillNode)
                } else {
                    cancelAutofillForNode(autofillNode)
                }
            }
        }
}

/**
 * Adds a vertical fading edge.
 */
fun Modifier.verticalFadingEdge(
    scrollState: ScrollState,
    length: Dp
) = composed {
    val color = MaterialTheme.colorScheme.surface

    drawWithContent {
        val lengthValue = length.toPx()
        val scrollFromTop = scrollState.value
        val scrollFromBottom = scrollState.maxValue - scrollState.value

        val topFadingEdgeStrength = lengthValue * (scrollFromTop / lengthValue)
            .coerceAtMost(1f)
        val bottomFadingEdgeStrength = lengthValue * (scrollFromBottom / lengthValue)
            .coerceAtMost(1f)

        drawContent()

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color,
                    Color.Transparent
                ),
                startY = 0f,
                endY = topFadingEdgeStrength
            ),
            size = Size(
                width = size.width,
                height = topFadingEdgeStrength
            )
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    color
                ),
                startY = size.height - bottomFadingEdgeStrength,
                endY = size.height
            ),
            topLeft = Offset(
                x = 0f,
                y = size.height - bottomFadingEdgeStrength
            )
        )
    }
}
