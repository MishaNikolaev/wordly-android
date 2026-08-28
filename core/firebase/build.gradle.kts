plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
	alias(libs.plugins.ksp)
	id("com.google.gms.google-services")
}

dependencies {
	implementation(platform(libs.firebase.bom))
	api(libs.firebase.auth)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.coroutines.android)
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")
	implementation(libs.dagger)
	implementation(projects.core.preferences)
	ksp(libs.dagger.compiler)
}
