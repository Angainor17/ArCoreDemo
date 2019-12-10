package com.simferopol.app.ui.about

import androidx.core.text.parseAsHtml
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.AboutCityInfo
import com.simferopol.app.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class AboutVm : ViewModel() {

    private val apiManager by App.kodein.instance<ApiManager>()

    val about = MutableLiveData<AboutCityInfo>()

    val contentText = MutableLiveData<String>()

    val player = MutableLiveData<Boolean>()

    init {
        GlobalScope.launch {
            val result = apiManager.getAbout()
            var content: String?
            if (result.success) {
                about.postValue(result.data)
                content = result.data?.content
                GlobalScope.launch(Dispatchers.Main) {
                    if (result.data?.audio != null) player.value = true
                }
                contentText.postValue(content?.parseAsHtml().toString())
            }
        }
    }
}