plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(libs.decompose)
	implementation(libs.decompose.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)

	implementation(projects.component.ui)
	implementation(projects.features.authorization.signin)
	implementation(projects.features.authorization.signup)
}
