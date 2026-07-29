package com.nmichail.wordly.android.features.words.add.presentation

import com.nmichail.wordly.android.features.words.domain.entity.WordExample
import com.nmichail.wordly.android.features.words.domain.entity.WordTag

data class AddWordDialogState(
	val wordInput: String,
	val phonetic: String?,
	val translation: String?,
	val definition: String?,
	val examples: List<WordExample>,
	val difficulty: Int,
	val selectedTagIds: Set<String>,
	val availableTags: List<WordTag>,
	val isLookingUp: Boolean,
	val isSubmitting: Boolean,
)