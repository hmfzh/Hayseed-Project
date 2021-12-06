package com.miham.hayseed.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences(val context: Context) {
    companion object {
        const val USER_PREFF = "USER_PREFF"
    }


    val sharedPreferences = context.getSharedPreferences(USER_PREFF, 0)

    fun setValues(key: String, value: String) {
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun getValues(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
}