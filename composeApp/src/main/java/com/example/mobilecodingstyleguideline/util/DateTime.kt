package com.example.mobilecodingstyleguideline.util

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTime {
    fun getCurrentDateTime(): String {
        val currentTime = ZonedDateTime.now(ZoneOffset.UTC)

        val timeInString = currentTime.toString()

        return timeInString
    }

    fun formatDateTime(date: String? = null): String {
        val dateTime = ZonedDateTime.parse(date)

        val formatter = DateTimeFormatter.ofPattern("eee dd MMM yyyy HH:mm:ss", Locale.ENGLISH)

        val formattedDateTime = dateTime.format(formatter)

        return formattedDateTime
    }
}