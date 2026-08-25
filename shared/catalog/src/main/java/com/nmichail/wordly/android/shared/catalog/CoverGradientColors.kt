package com.nmichail.wordly.android.shared.catalog

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlin.coroutines.resume
import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import android.graphics.Color as AndroidColor

data class CoverGradientColors(
	val top: Color,
	val bottom: Color,
)

@Composable
fun rememberCoverGradientColors(
	url: String?,
	fallbackTop: Color,
	fallbackBottom: Color,
	washToward: Color = Color.White,
): CoverGradientColors {
	val context = LocalContext.current.applicationContext
	val fallback = remember(fallbackTop, fallbackBottom) {
		CoverGradientColors(top = fallbackTop, bottom = fallbackBottom)
	}
	var colors by remember(url, fallbackTop, fallbackBottom, washToward) {
		mutableStateOf(fallback)
	}

	LaunchedEffect(url, fallbackTop, fallbackBottom, washToward) {
		if (url.isNullOrBlank()) {
			colors = fallback
			return@LaunchedEffect
		}
		val extracted = withContext(Dispatchers.IO) {
			val bitmap = try {
				loadBitmap(context = context, url = url)
			} catch (_: Exception) {
				null
			}
			bitmap?.let { extractSoftGradient(bitmap = it, washToward = washToward) }
		}
		colors = extracted ?: fallback
	}

	return colors
}

private suspend fun loadBitmap(
	context: android.content.Context,
	url: String,
): Bitmap? =
	suspendCancellableCoroutine { continuation ->
		val target = object : CustomTarget<Bitmap>() {
			override fun onResourceReady(
				resource: Bitmap,
				transition: Transition<in Bitmap>?,
			) {
				if (continuation.isActive) {
					continuation.resume(resource)
				}
			}

			override fun onLoadCleared(placeholder: Drawable?) = Unit

			override fun onLoadFailed(errorDrawable: Drawable?) {
				if (continuation.isActive) {
					continuation.resume(null)
				}
			}
		}
		continuation.invokeOnCancellation {
			Glide.with(context).clear(target)
		}
		Glide.with(context)
			.asBitmap()
			.load(url)
			.override(BITMAP_SIZE, BITMAP_SIZE)
			.centerCrop()
			.into(target)
	}

private fun extractSoftGradient(
	bitmap: Bitmap,
	washToward: Color,
): CoverGradientColors {
	val sample = sampleCoverColors(bitmap)
	return CoverGradientColors(
		top = softWash(color = sample.vibrant, toward = washToward),
		bottom = softWash(color = sample.average, toward = washToward, mix = WASH_BOTTOM),
	)
}

private data class CoverColorSample(
	val average: Color,
	val vibrant: Color,
)

private fun sampleCoverColors(bitmap: Bitmap): CoverColorSample {
	val width = bitmap.width.coerceAtLeast(1)
	val height = bitmap.height.coerceAtLeast(1)
	val step = max(1, min(width, height) / SAMPLE_DIVISOR)
	val accumulator = CoverColorAccumulator()

	var y = 0
	while (y < height) {
		var x = 0
		while (x < width) {
			accumulator.consume(bitmap.getPixel(x, y))
			x += step
		}
		y += step
	}

	return accumulator.toSample()
}

private class CoverColorAccumulator {
	private var sumR = 0L
	private var sumG = 0L
	private var sumB = 0L
	private var count = 0L
	private var bestSat = -1f
	private var vibrantR = 180
	private var vibrantG = 180
	private var vibrantB = 190

	fun consume(pixel: Int) {
		if (AndroidColor.alpha(pixel) < ALPHA_MIN) return
		val r = AndroidColor.red(pixel)
		val g = AndroidColor.green(pixel)
		val b = AndroidColor.blue(pixel)
		sumR += r
		sumG += g
		sumB += b
		count++

		val maxC = max(r, max(g, b)) / 255f
		val minC = min(r, min(g, b)) / 255f
		val sat = if (maxC <= 0.001f) 0f else (maxC - minC) / maxC
		if (sat > bestSat && maxC > BRIGHTNESS_MIN) {
			bestSat = sat
			vibrantR = r
			vibrantG = g
			vibrantB = b
		}
	}

	fun toSample(): CoverColorSample {
		val average = if (count > 0) {
			Color(
				red = (sumR / count) / 255f,
				green = (sumG / count) / 255f,
				blue = (sumB / count) / 255f,
			)
		} else {
			Color(red = 0.91f, green = 0.91f, blue = 0.93f)
		}
		return CoverColorSample(
			average = average,
			vibrant = Color(
				red = vibrantR / 255f,
				green = vibrantG / 255f,
				blue = vibrantB / 255f,
			),
		)
	}
}

private fun softWash(
	color: Color,
	toward: Color,
	mix: Float = WASH_TOP,
): Color =
	Color(
		red = color.red + (toward.red - color.red) * mix,
		green = color.green + (toward.green - color.green) * mix,
		blue = color.blue + (toward.blue - color.blue) * mix,
		alpha = 1f,
	)

private const val BITMAP_SIZE = 64
private const val SAMPLE_DIVISOR = 24
private const val ALPHA_MIN = 128
private const val BRIGHTNESS_MIN = 0.18f
private const val WASH_TOP = 0.52f
private const val WASH_BOTTOM = 0.68f