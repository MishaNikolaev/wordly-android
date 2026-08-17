package com.nmichail.wordly.android.features.constructor.practice.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import com.nmichail.wordly.android.component.wui.theme.isAppInDarkTheme

enum class ConstructorWordChipStyle {
    Bank,
    Answer,
}

@Composable
fun ConstructorWordChip(
    text: String,
    style: ConstructorWordChipStyle,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    val colorScheme = MaterialTheme.colorScheme
    val dark = isAppInDarkTheme()
    val (background, contentColor, borderColor) = when (style) {
        ConstructorWordChipStyle.Bank -> Triple(
            if (dark) colorScheme.surfaceVariant else colorScheme.surface,
            colorScheme.onSurface,
            colorScheme.outline,
        )

        ConstructorWordChipStyle.Answer -> Triple(
            colorScheme.onSurface,
            colorScheme.surface,
            colorScheme.onSurface,
        )
    }
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            lineHeight = 18.sp,
        ),
        color = contentColor,
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .then(clickableModifier)
            .padding(horizontal = 14.dp, vertical = 10.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun ConstructorWordChipPreview() {
    WuiTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ConstructorWordChip(text = "hello", style = ConstructorWordChipStyle.Bank, onClick = {})
            ConstructorWordChip(
                text = "world",
                style = ConstructorWordChipStyle.Answer,
                onClick = {})
        }
    }
}