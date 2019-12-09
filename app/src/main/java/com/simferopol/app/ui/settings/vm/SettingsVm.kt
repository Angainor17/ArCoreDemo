package com.simferopol.app.ui.settings.vm

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.GeoObject
import com.simferopol.api.models.LoadedFiles
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.BuildConfig
import com.simferopol.app.R
import com.simferopol.app.providers.lang.ILocaleProvider
import com.simferopol.app.providers.lang.LocaleItem
import com.simferopol.app.providers.lang.RU_LOCALE
import com.simferopol.app.providers.res.IResProvider
import com.simferopol.app.ui.settings.SettingsView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import java.io.File

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
        loadedFiles.value!!.monuments = true
        GlobalScope.launch(Dispatchers.IO) {
            var result = apiManager.getGeoObjects(1)
            if (result.success) {
                result.data?.forEach {
                    var audioUrl = it.audio
                    loadFile(audioUrl)
                }
                apiManager.setLoadedFiles(loadedFiles.value!!)
            }
        }
    }

    fun loadHistory() {
        loadedFiles.value!!.history = true
        GlobalScope.launch(Dispatchers.IO) {
            var result = apiManager.getStories()
            if (result.success) {
                result.data?.forEach {
                    var audioUrl = it.audio
                    loadFile(audioUrl)
                }
                apiManager.setLoadedFiles(loadedFiles.value!!)
            }
        }
    }

    fun initLoadedFiles() {
        GlobalScope.launch(Dispatchers.IO) {
            val result = apiManager.getLoadedFiles()
            if ((result.success) and (result.data != null))
                GlobalScope.launch(Dispatchers.Main) {
                    loadedFiles.value = result.data!!
                }
            else apiManager.setLoadedFiles(loadedFiles.value!!)
        }
    }

    fun loadFile(audioUrl: String?) {
        if (!audioUrl.isNullOrEmpty()) {
            var fileName = audioUrl.substring(audioUrl.lastIndexOf('/') + 1)
            var file =
                File(context.getExternalFilesDir(null).toString() + "/downloads/" + fileName)
            if (!file.exists()) {
                var request = DownloadManager.Request(Uri.parse(audioUrl))
                    .setDestinationInExternalFilesDir(
                        context,
                        "downloads",
                        fileName
                    )
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                val downloadManager =
                    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                var downloadID = downloadManager.enqueue(request)
            }
        }
    }
}