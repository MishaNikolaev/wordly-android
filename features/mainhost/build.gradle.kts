plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(libs.decompose)
	implementation(libs.decompose.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)

	implementation(projects.component.contract)
	implementation(projects.component.ui)
	implementation(projects.shared.authorization.contract)
}