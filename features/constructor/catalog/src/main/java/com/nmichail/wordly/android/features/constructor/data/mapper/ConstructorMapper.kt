package com.nmichail.wordly.android.features.constructor.data.mapper

import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorCatalogResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorLevelBannerResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorSectionResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorThemeResponse
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorCatalog
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorLevelBanner
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSection
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorTheme

fun ConstructorCatalogResponse.toEntity(): ConstructorCatalog =
    ConstructorCatalog(
        title = title,
        searchPlaceholder = searchPlaceholder,
        levelBanner = levelBanner?.toEntity(),
        sections = sections.map { it.toEntity() },
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