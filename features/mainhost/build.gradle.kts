plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(libs.decompose)
	implementation(libs.decompose.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)

	implementation(projects.ui)
	implementation(projects.signin)
	implementation(projects.signup)
	implementation(projects.home)
	implementation(projects.words)
	implementation(projects.stats)
	implementation(projects.profile)
}
