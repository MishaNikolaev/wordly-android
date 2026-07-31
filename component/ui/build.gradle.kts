plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material.icons.extended)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.glide)

	debugImplementation(libs.androidx.ui.tooling)

	implementation(projects.core.validation)
}
