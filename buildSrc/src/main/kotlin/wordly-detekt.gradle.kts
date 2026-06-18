import io.gitlab.arturbosch.detekt.Detekt

plugins {
	id("io.gitlab.arturbosch.detekt")
}

private val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

detekt {
	toolVersion = libs.findVersion("detekt").get().toString()

	source.setFrom("src")
	config.setFrom("$rootDir/buildsystem/detekt/detekt.yml")

	parallel = true
	ignoreFailures = false
}

tasks.withType<Detekt>().configureEach {
	jvmTarget = libs.findVersion("jvmTarget").get().requiredVersion
}

dependencies {
	"detektPlugins"(libs.findLibrary("detekt-formatting").get())
	"detektPlugins"(libs.findLibrary("detekt-rules-libraries").get())
}