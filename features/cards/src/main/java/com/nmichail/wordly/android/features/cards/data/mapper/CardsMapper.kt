package com.nmichail.wordly.android.features.cards.data.mapper

import com.nmichail.wordly.android.features.cards.data.dto.CardPracticeOptionResponse
import com.nmichail.wordly.android.features.cards.data.dto.CardPracticeWordResponse
import com.nmichail.wordly.android.features.cards.data.dto.CardSessionResponse
import com.nmichail.wordly.android.features.cards.data.dto.CardsItemResponse
import com.nmichail.wordly.android.features.cards.data.dto.CardsLevelBannerResponse
import com.nmichail.wordly.android.features.cards.data.dto.CardsResponse
import com.nmichail.wordly.android.features.cards.data.dto.CardsSectionResponse
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeOption
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.domain.entity.Cards
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem
import com.nmichail.wordly.android.features.cards.domain.entity.CardsLevelBanner
import com.nmichail.wordly.android.features.cards.domain.entity.CardsSection

fun CardsResponse.toEntity(): Cards =
	Cards(
		title = title,
		searchPlaceholder = searchPlaceholder,
		levelBanner = levelBanner?.toEntity(),
		sections = sections.map { it.toEntity() },
	)

fun CardSessionResponse.toEntity(): List<CardPracticeWord> =
	words.map { it.toEntity() }

private fun CardsLevelBannerResponse.toEntity(): CardsLevelBanner =
	CardsLevelBanner(
		text = text,
		levelLabel = levelLabel,
		levels = levels,
	)

private fun CardsSectionResponse.toEntity(): CardsSection =
	CardsSection(
		title = title,
		items = items.map { it.toEntity() },
	)

private fun CardsItemResponse.toEntity(): CardsItem =
	CardsItem(
		id = id,
		title = title,
		subtitle = subtitle,
		badge = badge,
		imageUrl = imageUrl,
	)

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
