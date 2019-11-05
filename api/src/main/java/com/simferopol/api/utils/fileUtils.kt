package com.simferopol.api.utils

import android.content.Context

fun readJson(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}
