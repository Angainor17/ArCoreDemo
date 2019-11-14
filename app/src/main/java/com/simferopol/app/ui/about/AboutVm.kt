package com.simferopol.app.ui.about

import android.util.Log
import androidx.core.text.parseAsHtml
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.models.AboutCityInfo
import com.simferopol.api.dataManager.DataManager
import com.simferopol.app.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class AboutVm : ViewModel() {

    private val routeManager by App.kodein.instance<DataManager>()

    val about = MutableLiveData<AboutCityInfo>()

    val contentText = MutableLiveData<String>()

    init {
        GlobalScope.launch {
            val result = routeManager.getAbout()
            var content: String?
            if (result.success) {
                about.postValue(result.data)
                content = result.data?.content
                contentText.postValue(content?.parseAsHtml().toString())
            }
        }
    }

    fun onPlayClick() {
        Log.e("play", "about")//todo play audio file
    }

}