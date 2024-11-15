package com.example.todo.utils

import android.content.Context
import android.content.SharedPreferences

object LanguagePreference {
    private const val PREF_NAME = "language_pref"
    private const val LANGUAGE_KEY = "language"

    fun saveLanguage(context: Context, language: String) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        preferences.edit().putString(LANGUAGE_KEY, language).apply()
    }

    fun getLanguage(context: Context): String {
        val preferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return preferences.getString(LANGUAGE_KEY, "en") ?: "en" // Mặc định là "en"
    }
}
