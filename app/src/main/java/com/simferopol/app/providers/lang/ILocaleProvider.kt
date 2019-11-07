package com.simferopol.app.providers.lang

import android.content.Context

interface ILocaleProvider {

    fun getLocale(): LocaleItem

    fun getAllLocales(): ArrayList<LocaleItem>

    fun setUpLocale(context: Context)

    fun setLocale(locale: LocaleItem)

}