package com.simferopol.api.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


const val FLOAT_PATTERN = "###,##0.00"
const val INT_PATTERN = "###,##0"

fun Float.format(pattern: String): String = format(this, pattern).replace(",",".")

fun Int.format(pattern: String): String = format(this, pattern)

fun Long.format(pattern: String): String = format(this, pattern)

private fun format(obj: Any, pattern: String) = DecimalFormat(pattern, getDecimalFormatSymbols()).format(obj)

private fun getDecimalFormatSymbols(): DecimalFormatSymbols {
    val dfs = DecimalFormatSymbols()
    dfs.groupingSeparator = ' '

    return dfs
}