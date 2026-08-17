package com.nmichail.wordly.android.features.books.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import com.nmichail.wordly.android.component.wui.theme.Wui

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookWordLookupDialog(
    word: String,
    translation: String,
    addButtonText: String,
    addedStatusText: String,
    added: Boolean,
    onAddClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    phonetic: String? = null,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BookWordSheetHandle() },
        modifier = modifier,
    ) {
        BookWordLookupSheetContent(
            word = word,
            phonetic = phonetic,
            translation = translation,
            addButtonText = addButtonText,
            addedStatusText = addedStatusText,
            added = added,
            onAddClick = onAddClick,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun BookWordLookupSheetContent(
    word: String,
    phonetic: String?,
    translation: String,
    addButtonText: String,
    addedStatusText: String,
    added: Boolean,
    onAddClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
			.fillMaxWidth()
			.navigationBarsPadding()
			.padding(horizontal = 20.dp)
			.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BookWordSheetHeader(
            word = word,
            phonetic = phonetic,
            onDismiss = onDismiss,
        )
        Text(
            text = translation,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        if (added) {
            BookWordAddedStatus(text = addedStatusText)
        } else {
            BookWordAddButton(
                text = addButtonText,
                onClick = onAddClick,
            )
        }
    }
}

@Composable
private fun BookWordSheetHandle() {
    Box(
        modifier = Modifier
			.padding(top = 10.dp)
			.size(width = 38.dp, height = 4.dp)
			.background(
				color = MaterialTheme.colorScheme.outlineVariant,
				shape = RoundedCornerShape(percent = 50),
			),
    )
}

@Composable
private fun BookWordSheetHeader(
    word: String,
    phonetic: String?,
    onDismiss: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
			.fillMaxWidth()
			.padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = word,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface,
            )
            if (!phonetic.isNullOrBlank()) {
                Text(
                    text = phonetic,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
        Box(
            modifier = Modifier
				.size(34.dp)
				.clip(RoundedCornerShape(10.dp))
				.background(colorScheme.surfaceContainerHigh)
				.clickable(
					role = Role.Button,
					onClick = onDismiss,
				),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                tint = colorScheme.onSurface,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun BookWordAddButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
			.fillMaxWidth()
			.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun BookWordAddedStatus(
    text: String,
) {
    val extended = Wui.colors
    BookWordAddedStatusRow(
        text = text,
        color = extended.success,
        containerColor = extended.successContainer,
    )
}

@Composable
private fun BookWordAddedStatusRow(
    text: String,
    color: Color,
    containerColor: Color,
) {
    Row(
        modifier = Modifier
			.fillMaxWidth()
			.height(50.dp)
			.background(
				color = containerColor,
				shape = RoundedCornerShape(14.dp),
			),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = color,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookWordLookupDialogPreview() {
    WuiTheme {
        BookWordLookupDialog(
            word = "apple",
            translation = "яблоко",
            phonetic = "/ˈæp.əl/",
            addButtonText = "Добавить",
            addedStatusText = "Добавлено",
            added = false,
            onAddClick = {},
            onDismiss = {},
        )
    }
}