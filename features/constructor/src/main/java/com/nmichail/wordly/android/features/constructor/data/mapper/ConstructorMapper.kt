package com.nmichail.wordly.android.features.constructor.data.mapper

import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorCatalogResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorLevelBannerResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorPhraseResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorSectionResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorSessionResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorThemeResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorWordResponse
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorCatalog
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorLevelBanner
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorPhrase
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSection
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorTheme
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorWord

fun ConstructorCatalogResponse.toEntity(): ConstructorCatalog =
	ConstructorCatalog(
		title = title,
		searchPlaceholder = searchPlaceholder,
		levelBanner = levelBanner?.toEntity(),
		sections = sections.map { it.toEntity() },
	)

fun ConstructorSessionResponse.toEntity(): ConstructorSession =
	ConstructorSession(
		themeId = themeId,
		themeTitle = themeTitle,
		phrases = phrases.map { it.toEntity() },
	)

private fun ConstructorLevelBannerResponse.toEntity(): ConstructorLevelBanner =
	ConstructorLevelBanner(
		text = text,
		levelLabel = levelLabel,
		levels = levels,
	)

private fun ConstructorSectionResponse.toEntity(): ConstructorSection =
	ConstructorSection(
		title = title,
		items = items.map { it.toEntity() },
	)

private fun ConstructorThemeResponse.toEntity(): ConstructorTheme =
	ConstructorTheme(
		id = id,
		title = title,
		subtitle = subtitle,
		badge = badge,
		imageUrl = imageUrl,
	)

private fun ConstructorPhraseResponse.toEntity(): ConstructorPhrase =
	ConstructorPhrase(
		id = id,
		question = question,
		author = author,
		words = words.map { it.toEntity() },
		correctOrder = correctOrder,
	)

private fun ConstructorWordResponse.toEntity(): ConstructorWord =
	ConstructorWord(
		id = id,
		text = text,
	)