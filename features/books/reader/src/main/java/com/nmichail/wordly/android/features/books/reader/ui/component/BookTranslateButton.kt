package com.nmichail.wordly.android.features.books.reader.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.books.reader.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import com.nmichail.wordly.android.component.wui.theme.WuiTheme

@Composable
fun BookTranslateButton(
    translating: Boolean,
    translated: Boolean,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val (background, iconTint) = when {
        translated -> colorScheme.primary to colorScheme.onPrimary
        else -> colorScheme.surfaceVariant to colorScheme.onBackground
    }

    Box(
        modifier = modifier
			.size(40.dp)
			.clip(RoundedCornerShape(12.dp))
			.background(background)
			.clickable(
				enabled = !translating,
				role = Role.Button,
				onClick = onClick,
			),
        contentAlignment = Alignment.Center,
    ) {
        if (translating) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = colorScheme.primary,
                strokeWidth = 2.dp,
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.paragraph),
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = Modifier.size(17.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookTranslateButtonPreview() {
    WuiTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BookTranslateButton(
                translating = false,
                translated = false,
                contentDescription = "Translate",
                onClick = {},
            )
            BookTranslateButton(
                translating = true,
                translated = false,
                contentDescription = "Translating",
                onClick = {},
            )
            BookTranslateButton(
                translating = false,
                translated = true,
                contentDescription = "Hide translation",
                onClick = {},
            )
        }
    }
}