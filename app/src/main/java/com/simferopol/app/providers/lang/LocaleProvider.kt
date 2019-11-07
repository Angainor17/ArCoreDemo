package com.simferopol.app.providers.lang

import android.content.Context
import com.google.gson.Gson
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.utils.storages.JsonDataStorage
import org.kodein.di.generic.instance
import java.util.*

const val LANGUAGE_KEY = "locale"
const val RU_LOCALE = "ru"
const val EN_LOCALE = "en"

class LocaleProvider : ILocaleProvider {

    private val jsonDataStorage by kodein.instance<JsonDataStorage>()

    private val rusLang = LocaleItem(RU_LOCALE)
    private val enLang = LocaleItem(EN_LOCALE)

    private val defaultLocale = enLang

    override fun getAllLocales(): ArrayList<LocaleItem> = arrayListOf(rusLang, enLang)

    override fun getLocale(): LocaleItem {
        val storageLanguage = getFromStorage()
        if (storageLanguage != null) {
            return storageLanguage
        }
        setLocale(defaultLocale)
        return defaultLocale
    }

    override fun setUpLocale(context: Context) {
        val res = context.resources

        val displayMetrics = res.displayMetrics
        val config = res.configuration
        config.locale = Locale(getLocale().locale)
        res.updateConfiguration(config, displayMetrics)
    }

    override fun setLocale(locale: LocaleItem) {
        jsonDataStorage.put(LANGUAGE_KEY, locale)
    }

    private fun getFromStorage(): LocaleItem? {
        return Gson().fromJson(jsonDataStorage.getJson(LANGUAGE_KEY), LocaleItem::class.java)
    }
}