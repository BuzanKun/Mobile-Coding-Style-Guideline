package com.example.mobilecodingstyleguideline.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTime {
    fun getCurrentDateTime(): String {
        val now = LocalDateTime.now()
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("eee dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
        val formattedDateTime = now.format(dateTimeFormatter)

        return formattedDateTime
    }
}