plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(libs.okhttp)
	implementation(projects.core.network)
}
