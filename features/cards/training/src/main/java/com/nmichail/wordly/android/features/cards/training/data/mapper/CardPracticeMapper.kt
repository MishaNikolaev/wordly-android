package com.nmichail.wordly.android.features.cards.training.data.mapper

import com.nmichail.wordly.android.features.cards.training.data.dto.CardPracticeOptionResponse
import com.nmichail.wordly.android.features.cards.training.data.dto.CardPracticeWordResponse
import com.nmichail.wordly.android.features.cards.training.data.dto.CardSessionResponse
import com.nmichail.wordly.android.features.cards.training.domain.entity.CardPracticeOption
import com.nmichail.wordly.android.features.cards.training.domain.entity.CardPracticeWord

fun CardSessionResponse.toEntity(): List<CardPracticeWord> =
	words.map { it.toEntity() }

private fun CardPracticeWordResponse.toEntity(): CardPracticeWord =
	CardPracticeWord(
		id = id,
		word = word,
		phonetic = phonetic,
		audioUrl = audioUrl,
		options = options.map { it.toEntity() },
		correctOptionId = correctOptionId,
	)

private fun CardPracticeOptionResponse.toEntity(): CardPracticeOption =
	CardPracticeOption(
		id = id,
		text = text,
	)
