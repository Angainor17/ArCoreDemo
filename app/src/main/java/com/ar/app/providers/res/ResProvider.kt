package com.ar.app.providers.res

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable

class ResProvider(private val resources: Resources) : IResProvider {

    override fun getText(stringRes: Int): CharSequence = resources.getText(stringRes)

    override fun getString(stringRes: Int): String = resources.getString(stringRes)

    override fun getQuantityString(stringRes: Int, quantity: Int, vararg formatArgs: Any) =
        resources.getQuantityString(stringRes, quantity, formatArgs)

    override fun getTextArray(arrayRes: Int): Array<CharSequence> = resources.getTextArray(arrayRes)

    override fun getStringArray(arrayRes: Int): Array<String> = resources.getStringArray(arrayRes)

    override fun getIntArray(arrayRes: Int): IntArray = resources.getIntArray(arrayRes)

    override fun getDimension(dimenRes: Int): Float = resources.getDimension(dimenRes)

    override fun getColor(colorRes: Int): Int = resources.getColor(colorRes)

    override fun getBoolean(boolRes: Int): Boolean = resources.getBoolean(boolRes)

    override fun getInteger(integerRes: Int): Int = resources.getInteger(integerRes)

    override fun getDrawable(drawableRes: Int): Drawable = resources.getDrawable(drawableRes)

    override fun getColorStateList(id: Int): ColorStateList = resources.getColorStateList(id)
}