package com.nmichail.wordly.android.features.cards.training.presentation

import com.nmichail.wordly.android.features.cards.training.domain.entity.CardPracticeWord
import kotlin.random.Random

internal fun CardPracticeWord.withShuffledOptions(seed: String): CardPracticeWord =
	copy(options = options.shuffled(Random(seed.hashCode().toLong())))
