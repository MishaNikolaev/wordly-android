plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(libs.decompose)
	implementation(libs.decompose.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material.icons.extended)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.dagger)
	implementation(libs.retrofit)
	implementation(libs.mvikotlin)
	implementation(libs.mvikotlin.main)
	implementation(libs.mvikotlin.logging)
	implementation(libs.mvikotlin.extensions.coroutines)
	ksp(libs.dagger.compiler)

	implementation(projects.component.wui)
	implementation(projects.component.presentation)
	implementation(projects.core.navigation)
	implementation(projects.core.network)
	implementation(projects.core.preferences)
	implementation(projects.shared.words.domain)

	testImplementation(projects.testutils)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.test.junit.api)
	testImplementation(libs.test.junit.engine)
	testImplementation(libs.test.junit.params)
	testImplementation(libs.mockito.core)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.mockito.junit.jupiter)
}
