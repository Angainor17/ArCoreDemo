package com.simferopol.app.ui.settings.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.BuildConfig
import com.simferopol.app.R
import com.simferopol.app.providers.lang.ILocaleProvider
import com.simferopol.app.providers.lang.LocaleItem
import com.simferopol.app.providers.lang.RU_LOCALE
import com.simferopol.app.providers.res.IResProvider
import com.simferopol.app.ui.settings.SettingsView
import org.kodein.di.generic.instance

class SettingsVm(val view: SettingsView) : ViewModel() {

    val localeProvider by kodein.instance<ILocaleProvider>()
    val resProvider by kodein.instance<IResProvider>()
    val сontext by kodein.instance<Context>()

    val appVersion = MutableLiveData("")

    val localeLiveData = MutableLiveData(LocaleItem(RU_LOCALE))

    init {
        initLocale()
        initVersionName()
    }

    private fun initVersionName() {
        appVersion.postValue(
            resProvider.getString(R.string.version) + " " +
                    BuildConfig.VERSION_NAME + " (" +
                    resProvider.getString(R.string.build) + " " +
                    BuildConfig.APP_BUILD_NUMBER +
                    ")"
        )
    }

    fun sendStatisticClick() {
        //TODO implement
    }

    fun changeLocale(locale: String) {
        if (localeLiveData.value?.locale == locale) return

        val localeItem = LocaleItem(locale)

        localeLiveData.postValue(localeItem)
        localeProvider.setLocale(localeItem)
        localeProvider.setUpLocale(сontext)

        view.recreate()
    }

    private fun initLocale() {
        localeLiveData.postValue(localeProvider.getLocale())
    }
}