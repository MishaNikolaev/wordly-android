package com.nmichail.wordly.android.features.constructor.practice.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import com.nmichail.wordly.android.component.wui.theme.Wui

@Composable
fun ConstructorAnswerBoard(
    correct: Boolean?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val extended = Wui.colors
    val colorScheme = MaterialTheme.colorScheme
    val (borderColor, background) = when (correct) {
        true -> extended.success to extended.successContainer
        false -> colorScheme.error to extended.errorContainer
        null -> extended.warning to extended.warningContainer
    }
    val shape = RoundedCornerShape(16.dp)
    val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp)
            .clip(shape)
            .background(background)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    style = Stroke(width = 2.dp.toPx(), pathEffect = dash),
                    cornerRadius = CornerRadius(16.dp.toPx()),
                )
            }
            .border(0.dp, Color.Transparent, shape)
            .padding(14.dp),
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun ConstructorAnswerBoardPreview() {
    WuiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ConstructorAnswerBoard(correct = null) {
                Text(text = "drop words here")
            }
            ConstructorAnswerBoard(correct = true) {
                Text(text = "correct answer")
            }
            ConstructorAnswerBoard(correct = false) {
                Text(text = "wrong answer")
            }
        }
    }
}