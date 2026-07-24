package com.nmichail.wordly.android.features.review.data.mapper

import com.nmichail.wordly.android.features.review.data.dto.ReviewOptionResponse
import com.nmichail.wordly.android.features.review.data.dto.ReviewWordResponse
import com.nmichail.wordly.android.features.review.domain.entity.ReviewOption
import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord

fun ReviewWordResponse.toEntity(): ReviewWord =
	ReviewWord(
		id = id,
		word = word,
		phonetic = phonetic,
		audioUrl = audioUrl,
		options = options.map { it.toEntity() },
		correctOptionId = correctOptionId,
	)

private fun ReviewOptionResponse.toEntity(): ReviewOption =
	ReviewOption(
		id = id,
		text = text,
	)