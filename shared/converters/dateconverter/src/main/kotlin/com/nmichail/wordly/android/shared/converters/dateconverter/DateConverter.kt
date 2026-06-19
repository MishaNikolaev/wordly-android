package com.nmichail.wordly.android.shared.converters.dateconverter

import java.text.DateFormatSymbols
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone

interface DateConverter {

	fun setFormat(date: String, pattern: DatePattern, zoneId: ZoneId = ZoneId.systemDefault()): String

	fun setFormat(date: Date, pattern: DatePattern, zoneId: ZoneId = ZoneId.systemDefault()): String

	fun getTimeZone(zoneId: ZoneId = ZoneId.systemDefault()): String

	fun convertToDate(date: String): Date

	fun convertToMonthName(monthIndex: String): String

	fun getExclusiveDate(date: Date): Date
}

class DateConverterImpl : DateConverter {

	override fun setFormat(date: String, pattern: DatePattern, zoneId: ZoneId): String =
		OffsetDateTime.ofInstant(date.toDate().toInstant(), zoneId).setFormat(pattern.pattern)

	override fun setFormat(date: Date, pattern: DatePattern, zoneId: ZoneId): String =
		OffsetDateTime.ofInstant(date.toInstant(), zoneId).setFormat(pattern.pattern)

	override fun getTimeZone(zoneId: ZoneId): String =
		TimeZone.getTimeZone(zoneId).getDisplayName(false, TimeZone.SHORT, Locale.getDefault())

	override fun convertToDate(date: String): Date =
		date.toDate()

	override fun convertToMonthName(monthIndex: String): String =
		DateFormatSymbols(Locale.getDefault()).months[monthIndex.toInt() - 1]

	override fun getExclusiveDate(date: Date): Date =
		date.getExclusive()

	private fun OffsetDateTime.setFormat(pattern: String): String =
		format(DateTimeFormatter.ofPattern(pattern))

	private fun String.toDate(): Date =
		Date(Instant.parse(this).toEpochMilli())

	private fun Date.getExclusive(): Date =
		Date(toInstant().plus(1, ChronoUnit.DAYS).toEpochMilli())
}

fun Date.plusSeconds(seconds: Long): Date =
	Date(toInstant().plusSeconds(seconds).toEpochMilli())