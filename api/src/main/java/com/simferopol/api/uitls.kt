package com.simferopol.api

import android.content.Context
import android.net.ConnectivityManager

fun hasConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager!!.activeNetworkInfo

    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}