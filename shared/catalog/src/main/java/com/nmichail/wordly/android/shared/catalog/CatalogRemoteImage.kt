package com.nmichail.wordly.android.shared.catalog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.nmichail.wordly.android.component.media.RemoteImage

@Composable
fun CatalogRemoteImage(
	url: String?,
	modifier: Modifier = Modifier,
) {
	RemoteImage(
		url = url,
		placeholderRes = R.drawable.frame,
		modifier = modifier.fillMaxSize(),
		contentScale = ContentScale.Crop,
	)
}