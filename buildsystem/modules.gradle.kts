val Modules = mapOf(
	"APP" to ":app",
	"FEATURE" to mapOf(
		"MAIN_HOST" to ":features:mainhost",
		"SIGN_IN" to ":features:authorization:signin",
		"SIGN_UP" to ":features:authorization:signup",
	),
	"CORE" to mapOf(
		"NAVIGATION" to ":core:navigation",
		"NETWORK" to ":core:network",
		"FAKE_NETWORK" to ":core:fakenetwork",
		"VALIDATION" to ":core:validation",
		"PREFERENCES" to ":core:preferences",
	),
	"SHARED" to mapOf(
		"AUTHORIZATION_CONTRACT" to ":shared:authorization:contract",
		"CONVERTERS_DATE" to ":shared:converters:dateconverter",
	),
	"COMPONENT" to mapOf(
		"UI" to ":component:ui",
		"CONTRACT" to ":component:contract",
	),
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

fun nameToPath(name: String): String = name.drop(1).replace(":", "/")

unpackMapValues(Modules) { moduleName ->
	include(moduleName)
	project(moduleName).projectDir = File(settingsDir, nameToPath(moduleName))
}