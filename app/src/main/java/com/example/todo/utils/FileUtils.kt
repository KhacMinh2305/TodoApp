package com.example.todo.utils

import java.text.SimpleDateFormat
import java.util.Locale

object FileUtils {

    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(timestamp)
    }
}
