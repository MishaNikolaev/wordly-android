val Versions = mapOf(
	"KOTLIN" to "2.0.21",
	"GRADLE" to "8.12.0",
	"DETEKT" to "1.23.8",

	"LIFECYCLE" to "2.8.7",
	"MATERIAL" to "1.13.0",
	"CORE" to "1.15.0",
	"APP_COMPAT" to "1.7.1",
	"ACTIVITY" to "1.9.3",
	"FRAGMENT" to "1.8.9",

	"COROUTINE" to "1.9.0",

	"DAGGER" to "2.56.2",

	"RETROFIT" to "3.0.0",
	"OKHTTP" to "5.2.0",
	"MOSHI" to "1.15.2",

	"DECOMPOSE" to "3.3.0",
	"ESSENTY" to "2.5.0",
	"MVIKOTLIN" to "4.3.0",

	"COMPOSE_ACTIVITY" to "1.9.3",
	"COMPOSE_MATERIAL" to "1.4.0",
	"COMPOSE" to "1.7.6",
	"COMPOSE_BOM" to "2024.12.01",

	"LOTTIE" to "6.6.10",
	"R8" to "8.10.24",

	"ANDROID_TOOLS_VERSION" to "2.1.5",

	"WORK_MANAGER" to "2.9.1",
)

val Deps = mapOf(
	"KOTLIN" to mapOf(
		"KOTLIN" to "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions["KOTLIN"]}",
		"KOTLIN_STANDARD_LIBRARY" to "org.jetbrains.kotlin:kotlin-stdlib:${Versions["KOTLIN"]}",
		"REFLECT" to "org.jetbrains.kotlin:kotlin-reflect:${Versions["KOTLIN"]}",
	),

	"COMPOSE_COMPILER" to "org.jetbrains.kotlin:compose-compiler-gradle-plugin:${Versions["KOTLIN"]}",

	"GRADLE" to "com.android.tools.build:gradle:${Versions["GRADLE"]}",

	"DETEKT" to mapOf(
		"GRADLE_PLUGIN" to "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions["DETEKT"]}",
		"FORMATTING" to "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions["DETEKT"]}",
		"RULES_LIBRARIES" to "io.gitlab.arturbosch.detekt:detekt-rules-libraries:${Versions["DETEKT"]}",
	),

	"LIFECYCLE" to mapOf(
		"LIFECYCLE_LIVEDATA_KTX" to "androidx.lifecycle:lifecycle-livedata-ktx:${Versions["LIFECYCLE"]}",
		"LIFECYCLE_RUNTIME" to "androidx.lifecycle:lifecycle-runtime:${Versions["LIFECYCLE"]}",
		"LIFECYCLE_RUNTIME_KTX" to "androidx.lifecycle:lifecycle-runtime-ktx:${Versions["LIFECYCLE"]}",
		"LIFECYCLE_COMMON_JAVA8" to "androidx.lifecycle:lifecycle-common-java8:${Versions["LIFECYCLE"]}",
		"LIFECYCLE_VIEWMODEL_KTX" to "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions["LIFECYCLE"]}",
	),

	"MATERIAL" to "com.google.android.material:material:${Versions["MATERIAL"]}",
	"CORE" to "androidx.core:core-ktx:${Versions["CORE"]}",
	"APP_COMPAT" to "androidx.appcompat:appcompat:${Versions["APP_COMPAT"]}",
	"ACTIVITY" to "androidx.activity:activity-ktx:${Versions["ACTIVITY"]}",
	"FRAGMENT" to "androidx.fragment:fragment-ktx:${Versions["FRAGMENT"]}",

	"COROUTINE" to mapOf(
		"COROUTINE_CORE" to "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions["COROUTINE"]}",
		"COROUTINE_ANDROID" to "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions["COROUTINE"]}",
	),

	"DAGGER" to mapOf(
		"CORE" to "com.google.dagger:dagger:${Versions["DAGGER"]}",
		"COMPILER" to "com.google.dagger:dagger-compiler:${Versions["DAGGER"]}",
	),

	"SQUAREUP" to mapOf(
		"RETROFIT" to "com.squareup.retrofit2:retrofit:${Versions["RETROFIT"]}",
		"RETROFIT_SCALARS" to "com.squareup.retrofit2:converter-scalars:${Versions["RETROFIT"]}",
		"OKHTTP_INTERCEPTOR" to "com.squareup.okhttp3:logging-interceptor:${Versions["OKHTTP"]}",
		"MOSHI_RETROFIT" to "com.squareup.retrofit2:converter-moshi:${Versions["RETROFIT"]}",
		"MOSHI" to "com.squareup.moshi:moshi-kotlin:${Versions["MOSHI"]}",
		"MOSHI_CODEGEN" to "com.squareup.moshi:moshi-kotlin-codegen:${Versions["MOSHI"]}",
		"MOSHI_ADAPTERS" to "com.squareup.moshi:moshi-adapters:${Versions["MOSHI"]}",
	),

	"COMPOSE_ACTIVITY" to "androidx.activity:activity-compose:${Versions["COMPOSE_ACTIVITY"]}",
	"COMPOSE_BOM" to "androidx.compose:compose-bom:${Versions["COMPOSE_BOM"]}",
	"COMPOSE" to mapOf(
		"MATERIAL" to "androidx.compose.material3:material3:${Versions["COMPOSE_MATERIAL"]}",
		"RUNTIME" to "androidx.compose.runtime:runtime:${Versions["COMPOSE"]}",
		"STUDIO_PREVIEW" to "androidx.compose.ui:ui-tooling-preview:${Versions["COMPOSE"]}",
		"TOOLING" to "androidx.compose.ui:ui-tooling:${Versions["COMPOSE"]}",
		"UI" to "androidx.compose.ui:ui:${Versions["COMPOSE"]}",
		"GRAPHICS" to "androidx.compose.ui:ui-graphics:${Versions["COMPOSE"]}",
	),

	"DECOMPOSE" to mapOf(
		"CORE" to "com.arkivanov.decompose:decompose:${Versions["DECOMPOSE"]}",
		"COMPOSE" to "com.arkivanov.decompose:extensions-compose:${Versions["DECOMPOSE"]}",
		"COROUTINES" to "com.arkivanov.essenty:lifecycle-coroutines:${Versions["ESSENTY"]}",
	),

	"MVIKOTLIN" to mapOf(
		"CORE" to "com.arkivanov.mvikotlin:mvikotlin:${Versions["MVIKOTLIN"]}",
		"MAIN" to "com.arkivanov.mvikotlin:mvikotlin-main:${Versions["MVIKOTLIN"]}",
		"EXTENSIONS_COROUTINES" to "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:${Versions["MVIKOTLIN"]}",
	),

	"LOTTIE" to "com.airbnb.android:lottie-compose:${Versions["LOTTIE"]}",

	"R8" to "com.android.tools:r8:${Versions["R8"]}",

	"ANDROID_TOOLS" to "com.android.tools:desugar_jdk_libs:${Versions["ANDROID_TOOLS_VERSION"]}",

	"WORK_MANAGER" to "androidx.work:work-runtime-ktx:${Versions["WORK_MANAGER"]}",
)

val VersionsForTests = mapOf(
	"KOTEST" to "5.9.1",

	"CORE" to "2.2.0",
	"LIFECYCLE" to "2.8.7",

	"MOCKITO_INLINE" to "5.2.0",
	"MOCKITO_JUNIT" to "5.13.0",
	"MOCKITO_CORE" to "5.14.0",
	"MOCKITO_KOTLIN" to "5.4.0",

	"TURBINE" to "1.1.0",

	"JUNIT" to "4.13.2",
	"JUNIT5" to "5.11.0",
	"JUNIT5_SUITE" to "1.11.0",

	"KASPRESSO" to "1.6.1",
	"KAKAO_COMPOSE" to "1.1.0",

	"ANDROIDX_TEST" to "1.6.1",
	"ANDROIDX_JUNIT" to "1.3.0",
)

val DepsForTests = mapOf(
	"KOTEST" to "io.kotest:kotest-runner-junit5-jvm:${VersionsForTests["KOTEST"]}",
	"COROUTINE" to "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions["COROUTINE"]}",

	"CORE" to "androidx.arch.core:core-testing:${VersionsForTests["CORE"]}",
	"LIFECYCLE" to "androidx.lifecycle:lifecycle-runtime-testing:${VersionsForTests["LIFECYCLE"]}",

	"MOCKITO" to mapOf(
		"MOCKITO_INLINE" to "org.mockito:mockito-inline:${VersionsForTests["MOCKITO_INLINE"]}",
		"MOCKITO_JUNIT" to "org.mockito:mockito-junit-jupiter:${VersionsForTests["MOCKITO_JUNIT"]}",
		"MOCKITO_CORE" to "org.mockito:mockito-core:${VersionsForTests["MOCKITO_CORE"]}",
		"MOCKITO_KOTLIN" to "org.mockito.kotlin:mockito-kotlin:${VersionsForTests["MOCKITO_KOTLIN"]}",
	),

	"TURBINE" to "app.cash.turbine:turbine:${VersionsForTests["TURBINE"]}",

	"JUNIT" to "junit:junit:${VersionsForTests["JUNIT"]}",

	"JUNIT5" to mapOf(
		"JUNIT_API" to "org.junit.jupiter:junit-jupiter-api:${VersionsForTests["JUNIT5"]}",
		"JUNIT_ENGINE" to "org.junit.jupiter:junit-jupiter-engine:${VersionsForTests["JUNIT5"]}",
		"JUNIT_PARAMS" to "org.junit.jupiter:junit-jupiter-params:${VersionsForTests["JUNIT5"]}",
		"JUNIT_SUITE" to "org.junit.platform:junit-platform-suite:${VersionsForTests["JUNIT5_SUITE"]}",
	),

	"KASPRESSO" to mapOf(
		"CORE" to "com.kaspersky.android-components:kaspresso:${VersionsForTests["KASPRESSO"]}",
		"COMPOSE" to "com.kaspersky.android-components:kaspresso-compose-support:${VersionsForTests["KASPRESSO"]}",
	),

	"KAKAO" to mapOf(
		"COMPOSE" to "io.github.kakaocup:compose:${VersionsForTests["KAKAO_COMPOSE"]}",
	),

	"COMPOSE_TEST" to mapOf(
		"JUNIT4" to "androidx.compose.ui:ui-test-junit4:${Versions["COMPOSE"]}",
		"MANIFEST" to "androidx.compose.ui:ui-test-manifest:${Versions["COMPOSE"]}",
	),

	"ANDROIDX_TEST" to mapOf(
		"CORE" to "androidx.test:core:${VersionsForTests["ANDROIDX_TEST"]}",
		"RULES" to "androidx.test:rules:${VersionsForTests["ANDROIDX_TEST"]}",
		"EXT_JUNIT" to "androidx.test.ext:junit:${VersionsForTests["ANDROIDX_JUNIT"]}",
	),
)

extra["Versions"] = Versions
extra["Deps"] = Deps
extra["VersionsForTests"] = VersionsForTests
extra["DepsForTests"] = DepsForTests