plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material.icons.extended)
	implementation(libs.androidx.ui.tooling.preview)

	debugImplementation(libs.androidx.ui.tooling)


	testImplementation(libs.test.junit.api)
	testImplementation(libs.test.junit.engine)
	testImplementation(libs.test.junit.params)
}
