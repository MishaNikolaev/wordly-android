plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(libs.gson)
	implementation(libs.retrofit)
	implementation(libs.dagger)
	implementation(projects.core.preferences)
	ksp(libs.dagger.compiler)

	testImplementation(projects.testutils)
	testImplementation(libs.test.junit.engine)
	testImplementation(libs.test.junit.params)
}
