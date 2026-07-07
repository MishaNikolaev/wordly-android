plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(libs.decompose)
	implementation(libs.decompose.compose)
	implementation(libs.mvikotlin)
	implementation(libs.mvikotlin.main)
	implementation(libs.mvikotlin.logging)
	implementation(libs.mvikotlin.extensions.coroutines)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material.icons.extended)

	implementation(projects.ui)
	implementation(projects.navigation)
	implementation(projects.validation)

	testImplementation(libs.test.junit.api)
	testImplementation(libs.test.junit.engine)
	testImplementation(libs.test.junit.params)
}
