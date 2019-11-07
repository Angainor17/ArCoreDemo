package com.simferopol.app.utils.storages

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class JsonDataStorage(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun <Type> put(key: String, data: Type) {
        sharedPreferences.edit().putString(key, Gson().toJson(data)).apply()
    }

    fun putBoolean(key: String, data: Boolean) {
        sharedPreferences.edit().putBoolean(key, data).apply()
    }

    fun putLong(key: String, data: Long) {
        sharedPreferences.edit().putLong(key, data).apply()
    }

    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)

    fun getLong(key: String) = sharedPreferences.getLong(key, 0L)

    fun getJson(key: String, default: String = ""): String? = try {
        sharedPreferences.getString(key, default)
    } catch (e: Exception) {
        default
    }
}