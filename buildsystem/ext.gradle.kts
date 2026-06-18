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

fun unpackMapDependencies(map: Map<String, Any>, action: (Any) -> Unit) {
	map.values.forEach(action)
}