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
	ksp(libs.dagger.compiler)

	implementation(projects.component.wui)
	implementation(projects.component.presentation)
	implementation(projects.core.navigation)
}
