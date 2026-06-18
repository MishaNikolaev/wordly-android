buildscript {
	repositories {
		google()
		mavenCentral()
	}
	dependencies {
		classpath(libs.gradle.android)
		classpath(libs.gradle.kotlin)
		classpath(libs.gradle.compose)
		classpath(libs.gradle.detekt)
	}
}

apply(from = "buildsystem/dependencies.gradle.kts")
