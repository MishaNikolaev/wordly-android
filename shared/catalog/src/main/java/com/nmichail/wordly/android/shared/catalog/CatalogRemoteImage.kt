package com.nmichail.wordly.android.shared.catalog

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide

@Composable
fun CatalogRemoteImage(
	url: String?,
	modifier: Modifier = Modifier,
) {
	val contentModifier = modifier.fillMaxSize()
	if (url.isNullOrBlank()) {
		Image(
			painter = painterResource(R.drawable.frame),
			contentDescription = null,
			modifier = contentModifier,
			contentScale = ContentScale.Crop,
		)
	} else {
		AndroidView(
			factory = { context ->
				ImageView(context).apply {
					scaleType = ImageView.ScaleType.CENTER_CROP
				}
			},
			modifier = contentModifier,
			update = { imageView ->
				Glide.with(imageView.context.applicationContext)
					.load(url)
					.centerCrop()
					.placeholder(R.drawable.frame)
					.error(R.drawable.frame)
					.into(imageView)
			},
		)
	}
}
