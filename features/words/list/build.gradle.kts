plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(libs.decompose)
	implementation(libs.decompose.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material.icons.extended)
	implementation(libs.dagger)
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
	api(projects.shared.words.domain)
	implementation(libs.retrofit)
}