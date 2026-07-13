plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(libs.decompose)
	implementation(libs.decompose.compose)
	implementation(libs.mvikotlin)
	implementation(libs.mvikotlin.main)
	implementation(libs.mvikotlin.logging)
	implementation(libs.mvikotlin.extensions.coroutines)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material.icons.extended)
	implementation(libs.dagger)
	implementation(libs.retrofit)
	ksp(libs.dagger.compiler)
	ksp(libs.scabbard.processor)

	implementation(projects.component.ui)
	implementation(projects.component.presentation)
	implementation(projects.core.navigation)
	implementation(projects.core.validation)
	implementation(projects.core.network)
	implementation(projects.core.preferences)
	implementation(projects.shared.error)

	testImplementation(projects.testutils)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.test.junit.api)
	testImplementation(libs.test.junit.engine)
	testImplementation(libs.test.junit.params)
	testImplementation(libs.mockito.core)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.test.turbine)
	testImplementation(libs.mockito.junit.jupiter)
}
