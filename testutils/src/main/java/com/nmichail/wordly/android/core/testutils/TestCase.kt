package com.nmichail.wordly.android.core.testutils

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class TestCase(
	val id: String,
	val title: String,
)