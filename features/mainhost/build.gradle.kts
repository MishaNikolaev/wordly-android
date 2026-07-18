plugins {
	alias(libs.plugins.wordlyAndroidLibrary)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(libs.decompose)
	implementation(libs.decompose.compose)
	implementation(libs.dagger)
	ksp(libs.dagger.compiler)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material.icons.extended)

	implementation(projects.component.ui)
	implementation(projects.features.authorization.signin)
	implementation(projects.features.authorization.signup)
	implementation(projects.features.dev.networkselection)
	implementation(projects.features.home)
	implementation(projects.features.news)
	implementation(projects.features.review)
	implementation(projects.features.words)
	implementation(projects.features.stats)
	implementation(projects.features.profile)
	implementation(projects.core.network)
	implementation(projects.core.preferences)
	implementation(projects.shared.error)
}
