plugins {
	`kotlin-dsl`
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

repositories {
	google()
	mavenCentral()
}

dependencies {
	implementation(libs.gradle.android)
	implementation(libs.gradle.kotlin)
	implementation(libs.gradle.compose)
	implementation(libs.gradle.detekt)
}
