plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
}

dependencies {
	implementation(libs.dagger)

	testImplementation(libs.test.junit.api)
	testImplementation(libs.test.junit.engine)
	testImplementation(libs.test.junit.params)
}