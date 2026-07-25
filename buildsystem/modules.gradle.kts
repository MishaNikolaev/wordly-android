include(
	":app",
	":testutils",

	":features:mainhost",
	":features:home",
	":features:news",
	":features:review",
	":features:cards",
	":features:constructor",
	":features:words",
	":features:stats",
	":features:profile",
	":features:authorization:signin",
	":features:authorization:signup",
	":features:dev:networkselection",

	":core:navigation",
	":core:network",
	":core:fakenetwork",
	":core:validation",
	":core:preferences",

	":shared:converters:dateconverter",
	":shared:error",

	":component:ui",
	":component:presentation",
)
