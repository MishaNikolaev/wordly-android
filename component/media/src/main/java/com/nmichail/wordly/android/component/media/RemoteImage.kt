package com.nmichail.wordly.android.component.media

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide

@Composable
fun RemoteImage(
	url: String?,
	@DrawableRes placeholderRes: Int,
	modifier: Modifier = Modifier,
	contentScale: ContentScale = ContentScale.Crop,
) {
	if (url.isNullOrBlank()) {
		Image(
			painter = painterResource(placeholderRes),
			contentDescription = null,
			modifier = modifier,
			contentScale = contentScale,
		)
	} else {
		AndroidView(
			factory = { context ->
				ImageView(context).apply {
					scaleType = contentScale.toImageViewScaleType()
				}
			},
			modifier = modifier,
			update = { imageView ->
				imageView.scaleType = contentScale.toImageViewScaleType()
				Glide.with(imageView)
					.load(url)
					.placeholder(placeholderRes)
					.error(placeholderRes)
					.into(imageView)
			},
		)
	}
}

private fun ContentScale.toImageViewScaleType(): ImageView.ScaleType =
	when (this) {
		ContentScale.Fit -> ImageView.ScaleType.FIT_CENTER
		ContentScale.FillBounds -> ImageView.ScaleType.FIT_XY
		ContentScale.Inside -> ImageView.ScaleType.CENTER_INSIDE
		else -> ImageView.ScaleType.CENTER_CROP
	}