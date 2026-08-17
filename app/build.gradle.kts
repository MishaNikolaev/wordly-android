plugins {
	alias(libs.plugins.wordlyAndroidApplication)
	alias(libs.plugins.ksp)
}

android {
	flavorDimensions += "env"
	productFlavors {
		create("mock") {
			dimension = "env"
			applicationIdSuffix = ".mock"
			versionNameSuffix = "-mock"
			buildConfigField("boolean", "DEV_ENABLED", "true")
		}
		create("dev") {
			dimension = "env"
			versionNameSuffix = "-dev"
			buildConfigField("boolean", "DEV_ENABLED", "true")
		}
	}
}

dependencies {
	implementation(projects.features.mainhost)
	implementation(projects.features.authorization.signin)
	implementation(projects.features.authorization.signup)
	implementation(projects.features.dev.networkselection)
	implementation(projects.features.home)
	implementation(projects.features.review)
	implementation(projects.features.cards.collection)
	implementation(projects.features.cards.training)
	implementation(projects.features.constructor.catalog)
	implementation(projects.features.constructor.practice)
	implementation(projects.features.books)
	implementation(projects.features.words.list)
	implementation(projects.features.materials)
	implementation(projects.features.profile)
	implementation(projects.component.wui)
	implementation(projects.core.network)
	implementation(projects.core.preferences)
	implementation(projects.core.fakenetwork)
	implementation(projects.core.validation)
	implementation(projects.shared.englishlevel)
	implementation(projects.shared.error)

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