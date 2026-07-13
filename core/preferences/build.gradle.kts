plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(libs.gson)
	implementation(libs.dagger)
	ksp(libs.dagger.compiler)
}
