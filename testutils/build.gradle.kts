plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	api(libs.kotlinx.coroutines.test)
	api(libs.test.junit.api)
	api(libs.mockito.core)
	api(libs.mockito.kotlin)
	api(libs.androidx.arch.core.testing)
	api(libs.decompose)
}