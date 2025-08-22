package com.example.mobilecodingstyleguideline.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTime {
    fun getCurrentDateTime(): Long {
        val currentTime = ZonedDateTime.now(ZoneOffset.UTC)

        return currentTime.toEpochSecond()
    }

    fun formatDateTime(date: Long = 0L): String? {
        val instant = Instant.ofEpochSecond(date)

        val dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())

        val formatter = DateTimeFormatter.ofPattern("eee dd MMM yyyy HH:mm:ss", Locale.ENGLISH)

        val formattedDateTime = dateTime.format(formatter)

        return formattedDateTime
    }
}