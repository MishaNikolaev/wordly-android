package com.nmichail.wordly.android.features.constructor.practice.data.mapper

import com.nmichail.wordly.android.features.constructor.practice.data.dto.ConstructorPhraseResponse
import com.nmichail.wordly.android.features.constructor.practice.data.dto.ConstructorSessionResponse
import com.nmichail.wordly.android.features.constructor.practice.data.dto.ConstructorWordResponse
import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorPhrase
import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorWord

fun ConstructorSessionResponse.toEntity(): ConstructorSession =
    ConstructorSession(
        themeId = themeId,
        themeTitle = themeTitle,
        phrases = phrases.map { it.toEntity() },
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