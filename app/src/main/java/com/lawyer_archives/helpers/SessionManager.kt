package com.lawyer_archives.helpers

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private var editor: SharedPreferences.Editor = pref.edit()

    companion object {
        private const val PREF_NAME = "LawyerArchivesPref"
        private const val PRIVATE_MODE = 0

        // Keys
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
    }

    // Login session
    fun createLoginSession(userId: String, username: String, email: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_EMAIL, email)
        editor.apply()
    }

    fun checkLogin(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserId(): String {
        return pref.getString(KEY_USER_ID, "") ?: ""
    }

    fun getUsername(): String {
        return pref.getString(KEY_USERNAME, "") ?: ""
    }

    fun getUserEmail(): String {
        return pref.getString(KEY_EMAIL, "") ?: ""
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()
    }
}