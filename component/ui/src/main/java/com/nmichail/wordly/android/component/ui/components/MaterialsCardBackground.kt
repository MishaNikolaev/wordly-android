@file:Suppress("MagicNumber")

package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import com.nmichail.wordly.android.component.ui.theme.isAppInDarkTheme

enum class MaterialsCardBackgroundStyle {
	Scatter,
	Cluster,
	EdgeCrop,
}

private val LightCoverBackground = Color(0xFFF1E5F6)
private val DarkCoverBackground = Color(0xFF2A1F33)

private val LightCircleLavender = Color(0xFFD8AAEA)
private val LightCircleLavenderLight = Color(0xFFDEB7EE)
private val LightCircleLavenderSoft = Color(0xFFDCB2ED)
private val LightCircleMauve = Color(0xFFB798C2)
private val LightCircleMauveDark = Color(0xFFB190BC)

private val DarkCircleLavender = Color(0xFF6B4A82)
private val DarkCircleLavenderLight = Color(0xFF7A5A90)
private val DarkCircleLavenderSoft = Color(0xFF5C3D72)
private val DarkCircleMauve = Color(0xFF4A3558)
private val DarkCircleMauveDark = Color(0xFF3F2C4C)

private const val EDGE_PADDING_FRACTION = 0.08f

fun materialsCardBackgroundStyleFor(id: String): MaterialsCardBackgroundStyle =
	when (kotlin.math.abs(id.hashCode()) % MaterialsCardBackgroundStyle.entries.size) {
		0 -> MaterialsCardBackgroundStyle.Scatter
		1 -> MaterialsCardBackgroundStyle.Cluster
		else -> MaterialsCardBackgroundStyle.EdgeCrop
	}

@Composable
fun MaterialsCardBackground(
	style: MaterialsCardBackgroundStyle,
	modifier: Modifier = Modifier,
) {
	val dark = isAppInDarkTheme()
	val coverBackground = if (dark) DarkCoverBackground else LightCoverBackground
	val colors = if (dark) {
		CoverCircleColors(
			lavender = DarkCircleLavender,
			lavenderLight = DarkCircleLavenderLight,
			lavenderSoft = DarkCircleLavenderSoft,
			mauve = DarkCircleMauve,
			mauveDark = DarkCircleMauveDark,
		)
	} else {
		CoverCircleColors(
			lavender = LightCircleLavender,
			lavenderLight = LightCircleLavenderLight,
			lavenderSoft = LightCircleLavenderSoft,
			mauve = LightCircleMauve,
			mauveDark = LightCircleMauveDark,
		)
	}

	Canvas(
		modifier = modifier
			.fillMaxSize()
			.background(coverBackground),
	) {
		when (style) {
			MaterialsCardBackgroundStyle.Scatter -> drawScatter(colors)
			MaterialsCardBackgroundStyle.Cluster -> drawCluster(colors)
			MaterialsCardBackgroundStyle.EdgeCrop -> drawEdgeCrop(colors)
		}
	}
}

private data class CoverCircleColors(
	val lavender: Color,
	val lavenderLight: Color,
	val lavenderSoft: Color,
	val mauve: Color,
	val mauveDark: Color,
)

private fun DrawScope.drawScatter(colors: CoverCircleColors) {
	val h = size.height
	drawContainedCircles(
		listOf(
			CircleSpec(colors.lavender, h * 0.26f, 0.12f, 0.42f),
			CircleSpec(colors.lavenderLight, h * 0.20f, 0.28f, 0.78f),
			CircleSpec(colors.mauve, h * 0.15f, 0.48f, 0.62f),
			CircleSpec(colors.lavenderSoft, h * 0.30f, 0.82f, 0.28f),
			CircleSpec(colors.mauveDark, h * 0.13f, 0.70f, 0.78f),
		),
	)
}

private fun DrawScope.drawCluster(colors: CoverCircleColors) {
	val h = size.height
	drawContainedCircles(
		listOf(
			CircleSpec(colors.lavenderSoft, h * 0.28f, 0.20f, 0.28f),
			CircleSpec(colors.mauveDark, h * 0.13f, 0.14f, 0.82f),
			CircleSpec(colors.mauve, h * 0.12f, 0.50f, 0.22f),
			CircleSpec(colors.lavenderLight, h * 0.20f, 0.58f, 0.78f),
			CircleSpec(colors.mauve, h * 0.15f, 0.86f, 0.30f),
		),
	)
}

private fun DrawScope.drawEdgeCrop(colors: CoverCircleColors) {
	val w = size.width
	val h = size.height
	drawCircle(
		color = colors.lavenderSoft,
		radius = h * 0.62f,
		center = Offset(x = w * 0.06f, y = h * 0.02f),
	)
	drawCircle(
		color = colors.lavender,
		radius = h * 0.34f,
		center = Offset(x = w * 0.20f, y = h * 1.12f),
	)
	drawCircle(
		color = colors.lavenderLight,
		radius = h * 0.38f,
		center = Offset(x = w * 0.62f, y = h * -0.02f),
	)
	drawCircle(
		color = colors.mauve,
		radius = h * 0.38f,
		center = Offset(x = w * 0.92f, y = h * 0.98f),
	)
}

private data class CircleSpec(
	val color: Color,
	val radius: Float,
	val xFraction: Float,
	val yFraction: Float,
)

private fun DrawScope.drawContainedCircles(circles: List<CircleSpec>) {
	val padding = size.minDimension * EDGE_PADDING_FRACTION
	val minX = padding
	val minY = padding
	val maxX = size.width - padding
	val maxY = size.height - padding
	val maxRadius = minOf(maxX - minX, maxY - minY) / 2f

	circles.forEach { spec ->
		val radius = spec.radius.coerceAtMost(maxRadius)
		val center = Offset(
			x = (size.width * spec.xFraction).coerceIn(minX + radius, maxX - radius),
			y = (size.height * spec.yFraction).coerceIn(minY + radius, maxY - radius),
		)
		drawCircle(color = spec.color, radius = radius, center = center)
	}
}

@Preview(showBackground = true, widthDp = 378)
@Composable
private fun MaterialsCardBackgroundPreview() {
	WordlyAndroidTheme {
		Column(
			modifier = Modifier.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
		) {
			MaterialsCardBackgroundStyle.entries.forEach { style ->
				MaterialsCardBackground(
					style = style,
					modifier = Modifier
						.fillMaxWidth()
						.height(140.dp),
				)
			}
		}
	}
}
