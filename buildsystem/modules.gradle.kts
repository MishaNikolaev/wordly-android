val Modules = mapOf(
	"APP" to ":app",
	"FEATURE" to mapOf(
		"MAIN_HOST" to ":mainhost",
		"HOME" to ":home",
		"WORDS" to ":words",
		"STATS" to ":stats",
		"PROFILE" to ":profile",
		"SIGN_IN" to ":signin",
		"SIGN_UP" to ":signup",
	),
	"CORE" to mapOf(
		"NAVIGATION" to ":navigation",
		"NETWORK" to ":network",
		"FAKE_NETWORK" to ":fakenetwork",
		"VALIDATION" to ":validation",
		"PREFERENCES" to ":preferences",
	),
	"SHARED" to mapOf(
		"CONVERTERS_DATE" to ":dateconverter",
	),
	"COMPONENT" to mapOf(
		"UI" to ":ui",
	),
)

private val ModuleDirs = mapOf(
	":app" to "app",
	":mainhost" to "features/mainhost",
	":home" to "features/home",
	":words" to "features/words",
	":stats" to "features/stats",
	":profile" to "features/profile",
	":signin" to "features/authorization/signin",
	":signup" to "features/authorization/signup",
	":navigation" to "core/navigation",
	":network" to "core/network",
	":fakenetwork" to "core/fakenetwork",
	":validation" to "core/validation",
	":preferences" to "core/preferences",
	":dateconverter" to "shared/converters/dateconverter",
	":ui" to "component/ui",
)

fun unpackMapValues(map: Map<String, Any>, action: (String) -> Unit) {
	map.values.forEach { value ->
		when (value) {
			is String -> action(value)
			is Map<*, *> -> @Suppress("UNCHECKED_CAST")
			unpackMapValues(value as Map<String, Any>, action)
		}
	}
}

unpackMapValues(Modules) { moduleName ->
	include(moduleName)
	project(moduleName).projectDir = File(settingsDir, ModuleDirs.getValue(moduleName))
}
