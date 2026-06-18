import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.compose")
	id("wordly-detekt")
}

private val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
	namespace = libs.findVersion("project-namespace").get().requiredVersion
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
		applicationId = libs.findVersion("project-applicationId").get().requiredVersion
		minSdk = libs.findVersion("project-minSdk").get().requiredVersion.toInt()
		targetSdk = libs.findVersion("project-targetSdk").get().requiredVersion.toInt()
		versionCode = libs.findVersion("project-versionCode").get().requiredVersion.toInt()
		versionName = libs.findVersion("project-versionName").get().requiredVersion

		multiDexEnabled = true
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro",
			)
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
}