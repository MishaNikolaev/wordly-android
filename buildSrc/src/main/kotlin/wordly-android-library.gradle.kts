import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.compose")
	id("wordly-detekt")
}

private val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
	namespace = "com.nmichail.wordly.android" + project.path.replace(":", ".")
	compileSdk = libs.findVersion("project-compileSdk").get().requiredVersion.toInt()

	packaging {
		resources {
			excludes.add("META-INF/DEPENDENCIES")
			excludes.add("META-INF/*.kotlin_module")
			excludes.add("META-INF/LICENSE*")
			excludes.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
		}
	}

	defaultConfig {
		minSdk = libs.findVersion("project-minSdk").get().requiredVersion.toInt()

		multiDexEnabled = true
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		vectorDrawables {
			useSupportLibrary = true
		}
	}

	lint {
		abortOnError = false
	}

	compileOptions {
		val jvmVersion = libs.findVersion("jvmTarget").get().requiredVersion
		sourceCompatibility = JavaVersion.toVersion(jvmVersion)
		targetCompatibility = JavaVersion.toVersion(jvmVersion)
	}

	kotlin {
		jvmToolchain(17)
		compilerOptions {
			val jvmVersion = libs.findVersion("jvmTarget").get().requiredVersion
			jvmTarget.set(JvmTarget.fromTarget(jvmVersion))
		}
	}

	buildFeatures {
		buildConfig = true
		compose = true
	}
}

tasks.named("preBuild") {
	dependsOn("detekt")
}

dependencies {
	implementation(libs.findLibrary("kotlin-stdlib").get())
	implementation(libs.findLibrary("compose-runtime").get())
}