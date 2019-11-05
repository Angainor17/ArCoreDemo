package com.simferopol.api.utils

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun hasConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager!!.activeNetworkInfo

    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)