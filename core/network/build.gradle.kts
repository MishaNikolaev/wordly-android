plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	api(libs.okhttp)
	api(libs.retrofit)
	api(libs.retrofit.converter.gson)
	implementation(libs.gson)
	implementation(libs.dagger)
	implementation(libs.kotlinx.coroutines.core)
}
