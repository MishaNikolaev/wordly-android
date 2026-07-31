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

enum class MaterialsCardBackgroundStyle {
	Scatter,
	Cluster,
	EdgeCrop,
}

private val CoverBackground = Color(0xFFF1E5F6)

private val CircleLavender = Color(0xFFD8AAEA)
private val CircleLavenderLight = Color(0xFFDEB7EE)
private val CircleLavenderSoft = Color(0xFFDCB2ED)
private val CircleMauve = Color(0xFFB798C2)
private val CircleMauveDark = Color(0xFFB190BC)

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
	Canvas(
		modifier = modifier
			.fillMaxSize()
			.background(CoverBackground),
	) {
		when (style) {
			MaterialsCardBackgroundStyle.Scatter -> drawScatter()
			MaterialsCardBackgroundStyle.Cluster -> drawCluster()
			MaterialsCardBackgroundStyle.EdgeCrop -> drawEdgeCrop()
		}
	}
}

private fun DrawScope.drawScatter() {
	val h = size.height
	drawContainedCircles(
		listOf(
			CircleSpec(CircleLavender, h * 0.26f, 0.12f, 0.42f),
			CircleSpec(CircleLavenderLight, h * 0.20f, 0.28f, 0.78f),
			CircleSpec(CircleMauve, h * 0.15f, 0.48f, 0.62f),
			CircleSpec(CircleLavenderSoft, h * 0.30f, 0.82f, 0.28f),
			CircleSpec(CircleMauveDark, h * 0.13f, 0.70f, 0.78f),
		),
	)
}

private fun DrawScope.drawCluster() {
	val h = size.height
	drawContainedCircles(
		listOf(
			CircleSpec(CircleLavenderSoft, h * 0.28f, 0.20f, 0.28f),
			CircleSpec(CircleMauveDark, h * 0.13f, 0.14f, 0.82f),
			CircleSpec(CircleMauve, h * 0.12f, 0.50f, 0.22f),
			CircleSpec(CircleLavenderLight, h * 0.20f, 0.58f, 0.78f),
			CircleSpec(CircleMauve, h * 0.15f, 0.86f, 0.30f),
		),
	)
}

private fun DrawScope.drawEdgeCrop() {
	val w = size.width
	val h = size.height
	drawCircle(
		color = CircleLavenderSoft,
		radius = h * 0.62f,
		center = Offset(x = w * 0.06f, y = h * 0.02f),
	)
	drawCircle(
		color = CircleLavender,
		radius = h * 0.34f,
		center = Offset(x = w * 0.20f, y = h * 1.12f),
	)
	drawCircle(
		color = CircleLavenderLight,
		radius = h * 0.38f,
		center = Offset(x = w * 0.62f, y = h * -0.02f),
	)
	drawCircle(
		color = CircleMauve,
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
