package com.simferopol.app.ui.about

import android.util.Log
import androidx.core.text.parseAsHtml
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.models.About
import com.simferopol.api.routeManager.RouteManager
import com.simferopol.app.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class AboutVm : ViewModel() {

    private val routeManager by App.kodein.instance<RouteManager>()

    val about = MutableLiveData<About>()

    val contentText = MutableLiveData<String>()

    init {
        GlobalScope.launch {
            val result = routeManager.getAbout()
            if (result.success) {
                about.postValue(result.data)
                contentText.postValue(result.data?.content?.parseAsHtml().toString())
            }
        }
    }

    fun onPlayClick() {
        Log.e("play", "about")//todo play audio file
    }

}