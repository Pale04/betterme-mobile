package com.betterdevs.betterme.data.shared

import android.content.Context
import androidx.core.content.edit

object SharedPreferencesHelper {
    private const val PREFERENCES_NAME = "app_preferences"
    private const val AUTH_TOKEN_KEY = "token"

    fun saveAuthToken(context: Context, token: String) {
        val authTokenExists = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .contains(AUTH_TOKEN_KEY)
        if (authTokenExists) {
            clearAuthToken(context)
        }
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(AUTH_TOKEN_KEY, token)
            }
    }

    fun getAuthToken(context: Context): String? = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        .getString(AUTH_TOKEN_KEY, "")

    private fun clearAuthToken(context: Context) = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        .edit {
            remove(AUTH_TOKEN_KEY)
        }
}