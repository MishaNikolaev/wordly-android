import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("java-library")
	id("org.jetbrains.kotlin.jvm")
	id("wordly-detekt")
}

private val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

kotlin {
	compilerOptions {
		val jvmVersion = libs.findVersion("jvmTarget").get().toString()
		jvmTarget.set(JvmTarget.fromTarget(jvmVersion))
	}
}

dependencies {
	implementation(libs.findLibrary("kotlin-stdlib").get())
}