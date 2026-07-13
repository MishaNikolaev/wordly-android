plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(libs.decompose)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.mvikotlin)
	implementation(libs.mvikotlin.extensions.coroutines)
	implementation(projects.core.navigation)
}