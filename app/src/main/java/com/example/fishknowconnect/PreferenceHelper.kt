package com.example.fishknowconnect

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


class PreferenceHelper private constructor() {
    val PREF_LOGGEDIN_USER_USERNAME = "logged_in_Username"
    val PREF_USER_LOGGEDIN_STATUS = "logged_in_status"
    companion object {
        @Volatile
        private var instance: PreferenceHelper? = null
        lateinit var sharedPreference: SharedPreferences

        fun getInstance(ctx: Context): PreferenceHelper {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = PreferenceHelper()
                         sharedPreference = PreferenceManager.getDefaultSharedPreferences(ctx)
                    }
                }
            }
            return instance!!
        }
    }


    fun setLoggedInUserUsername(username: String?) {
        val editor = sharedPreference.edit()
        editor.putString(PREF_LOGGEDIN_USER_USERNAME, username)
        editor.commit()
    }

    fun getLoggedInUsernameUser(): String{
        return sharedPreference.getString(PREF_LOGGEDIN_USER_USERNAME, "") ?: ""
    }

    fun setUserLoggedInStatus(status: Boolean) {
        val editor = sharedPreference.edit()
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status)
        editor.commit()
    }

    fun getUserLoggedInStatus(): Boolean {
        return sharedPreference.getBoolean(PREF_USER_LOGGEDIN_STATUS, false)
    }

    fun clearLoggedInUsername() {
        val editor = sharedPreference.edit()
        editor.remove(PREF_LOGGEDIN_USER_USERNAME)
        editor.remove(PREF_USER_LOGGEDIN_STATUS)
        editor.commit()
    }
}