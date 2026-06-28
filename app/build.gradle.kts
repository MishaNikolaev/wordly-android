plugins {
	alias(libs.plugins.wordlyAndroidApplication)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(projects.mainhost)
	implementation(projects.component.ui)

	implementation(libs.decompose)
	implementation(libs.decompose.compose)

	implementation(libs.dagger)
	ksp(libs.dagger.compiler)

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)

	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)

	testImplementation(libs.test.junit.api)
	testImplementation(libs.test.junit.engine)
	testImplementation(libs.test.junit.params)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.test.core)
}