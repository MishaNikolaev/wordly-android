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

tasks.register("allUnitTests") {
	group = "verification"
	description = "Runs all JVM and Android debug unit tests"
}

gradle.projectsEvaluated {
	subprojects.forEach { project ->
		val allUnitTests = rootProject.tasks.named("allUnitTests")

		if (project.pluginManager.hasPlugin("org.jetbrains.kotlin.jvm")) {
			allUnitTests.configure { dependsOn(project.tasks.named("test")) }
		}

		if (
			project.pluginManager.hasPlugin("com.android.library") ||
			project.pluginManager.hasPlugin("com.android.application")
		) {
			project.tasks.findByName("testDebugUnitTest")?.let { testTask ->
				allUnitTests.configure { dependsOn(testTask) }
			}
		}
	}
}