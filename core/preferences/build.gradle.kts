plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(libs.gson)
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.datastore.preferences)
	implementation(libs.dagger)
	implementation(libs.kotlinx.coroutines.core)
	ksp(libs.dagger.compiler)
}