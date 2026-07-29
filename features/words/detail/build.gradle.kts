plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material.icons.extended)

	implementation(projects.component.ui)
	implementation(projects.shared.words.domain)
}
