package com.problemsolver.event.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDateTime(dateTimeString: String): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE MMMM yyyy HH:mm", Locale.getDefault())

        val date: Date = inputFormat.parse(dateTimeString) ?: Date()
        return outputFormat.format(date)
    } catch (e: Exception) {
        return dateTimeString
    }
}