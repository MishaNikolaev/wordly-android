package com.nmichail.wordly.android.features.constructor.practice.presentation

import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorWord
import kotlin.random.Random

internal fun List<ConstructorWord>.shuffledBank(seed: String): List<ConstructorWord> =
	shuffled(Random(seed.hashCode().toLong()))
