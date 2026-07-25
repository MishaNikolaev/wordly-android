package com.nmichail.wordly.android.features.constructor.domain.entity

data class ConstructorCatalog(
	val title: String,
	val searchPlaceholder: String,
	val levelBanner: ConstructorLevelBanner?,
	val sections: List<ConstructorSection>,
)