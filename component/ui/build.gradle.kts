plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)
}
