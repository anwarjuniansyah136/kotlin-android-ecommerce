package com.example.ecommerce.api.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val editor = prefs.edit()
    companion object {
        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context).also { INSTANCE = it }
            }
        }
    }
    fun saveAuthToken(token: String) {
        editor.putString("AUTH_TOKEN", token)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString("AUTH_TOKEN", null)
    }

    fun clearSession() {
        editor.clear()
        editor.apply()
    }
}
