package com.example.fishknowconnect

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


object PreferenceHelper {
    const val PREF_LOGGEDIN_USER_USERNAME = "logged_in_Username"
    const val PREF_USER_LOGGEDIN_STATUS = "logged_in_status"
    fun getSharedPreferences(ctx: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setLoggedInUserUsername(ctx: Context, username: String?) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(PREF_LOGGEDIN_USER_USERNAME, username)
        editor.commit()
    }

    fun getLoggedInUsernameUser(ctx: Context): String? {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_USERNAME, "")
    }

    fun setUserLoggedInStatus(ctx: Context, status: Boolean) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status)
        editor.commit()
    }

    fun getUserLoggedInStatus(ctx: Context): Boolean {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false)
    }

    fun clearLoggedInUsername(ctx: Context) {
        val editor = getSharedPreferences(ctx).edit()
        editor.remove(PREF_LOGGEDIN_USER_USERNAME)
        editor.remove(PREF_USER_LOGGEDIN_STATUS)
        editor.commit()
    }
}