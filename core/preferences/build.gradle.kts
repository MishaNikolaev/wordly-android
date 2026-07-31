plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(libs.gson)
	implementation(libs.dagger)
	implementation(libs.kotlinx.coroutines.core)
	ksp(libs.dagger.compiler)
}
