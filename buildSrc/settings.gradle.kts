pluginManagement {
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
	}
}

rootProject.name = "buildSrc"

dependencyResolutionManagement {
	repositories {
		google()
		mavenCentral()
	}
	versionCatalogs {
		create("libs") {
			from(files("../buildsystem/libs.versions.toml"))
		}
	}
}