package com.simferopol.app.ui.settings.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.LoadedFiles
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.BuildConfig
import com.simferopol.app.R
import com.simferopol.app.providers.lang.ILocaleProvider
import com.simferopol.app.providers.lang.LocaleItem
import com.simferopol.app.providers.lang.RU_LOCALE
import com.simferopol.app.providers.res.IResProvider
import com.simferopol.app.ui.settings.SettingsView
import com.simferopol.app.utils.CustomFileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class SettingsVm(val view: SettingsView) : ViewModel() {

    private val localeProvider by kodein.instance<ILocaleProvider>()
    private val resProvider by kodein.instance<IResProvider>()
    private val context by kodein.instance<Context>()
    private val apiManager by kodein.instance<ApiManager>()

    var loadedFiles = MutableLiveData<LoadedFiles>(LoadedFiles(false, false))

    val appVersion = MutableLiveData("")

    val localeLiveData = MutableLiveData(LocaleItem(RU_LOCALE))

    init {
        initLocale()
        initVersionName()
        initLoadedFiles()
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
        localeProvider.setUpLocale(context)

        view.recreate()
    }

    private fun initLocale() {
        localeLiveData.postValue(localeProvider.getLocale())
    }

    fun loadMonuments() {
        loadedFiles.value?.let {
            it.monuments = true
            GlobalScope.launch(Dispatchers.IO) {
                var result = apiManager.getGeoObjects(1)
                if (result.success) {
                    result.data?.forEach { geoObject ->
                        var audioUrl = geoObject.audio
                        CustomFileUtils().loadFile(context, audioUrl)
                    }
                    apiManager.setLoadedFiles(it)
                }
            }
        }
    }

    fun loadHistory() {
        loadedFiles.value?.let {
            it.history = true
            GlobalScope.launch(Dispatchers.IO) {
                val result = apiManager.getStories()
                if (result.success) {
                    result.data?.forEach { story ->
                        val audioUrl = story.audio
                        CustomFileUtils().loadFile(context, audioUrl)
                    }
                    apiManager.setLoadedFiles(it)
                }
            }
        }
    }

    fun initLoadedFiles() {
        GlobalScope.launch(Dispatchers.IO) {
            val result = apiManager.getLoadedFiles()
            if ((result.success) and (result.data != null))
                GlobalScope.launch(Dispatchers.Main) {
                    result.data?.let {
                        loadedFiles.value = it
                    }
                }
            else loadedFiles.value?.let { apiManager.setLoadedFiles(it) }
        }
    }
}