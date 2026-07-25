package com.nmichail.wordly.android.features.constructor.data.dto

data class ConstructorCatalogResponse(
	val title: String,
	val searchPlaceholder: String,
	val levelBanner: ConstructorLevelBannerResponse?,
	val sections: List<ConstructorSectionResponse>,
)