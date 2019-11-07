package com.simferopol.app.providers.res

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable

interface IResProvider {
    fun getText(stringRes: Int): CharSequence
    fun getString(stringRes: Int): String
    fun getQuantityString(stringRes: Int, quantity: Int): String
    fun getTextArray(arrayRes: Int): Array<CharSequence>
    fun getStringArray(arrayRes: Int): Array<String>
    fun getIntArray(arrayRes: Int): IntArray
    fun getColorStateList(id: Int): ColorStateList
    fun getDimension(dimenRes: Int): Float
    fun getColor(colorRes: Int): Int
    fun getBoolean(boolRes: Int): Boolean
    fun getInteger(integerRes: Int): Int
    fun getDrawable(drawableRes: Int): Drawable
}