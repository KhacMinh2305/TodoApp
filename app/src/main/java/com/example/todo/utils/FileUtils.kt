package com.example.todo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {

    private const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm"


    fun parseTime(dateTime: String): Long {
        val dateFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return dateFormat.parse(dateTime)?.time ?: 0L
    }

    fun formatTime(timestamp: Long): String {
        val dateFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return dateFormat.format(timestamp)
    }
}
