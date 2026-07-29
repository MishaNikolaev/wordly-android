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

	implementation(projects.component.ui)
	implementation(projects.component.presentation)
	implementation(projects.core.navigation)
	implementation(projects.core.network)
	implementation(projects.features.words.add)
	implementation(projects.features.words.detail)
	implementation(projects.shared.words.domain)
	implementation(libs.retrofit)
}
