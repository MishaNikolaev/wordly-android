pluginManagement {
	repositories {
		google {
			content {
				includeGroupByRegex("com\\.android.*")
				includeGroupByRegex("com\\.google.*")
				includeGroupByRegex("androidx.*")
			}
		}
		mavenCentral()
		gradlePluginPortal()
	}
	plugins {
		id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention")
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
	}
	versionCatalogs {
		create("libs") {
			from(files(File(settingsDir, "buildsystem/libs.versions.toml")))
		}
	}
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "wordly_android"

apply(from = "buildsystem/modules.gradle.kts")