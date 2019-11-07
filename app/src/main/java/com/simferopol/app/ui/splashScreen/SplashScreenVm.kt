package com.simferopol.app.ui.splashScreen

import android.content.Context
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.providers.lang.ILocaleProvider
import org.kodein.di.generic.instance

class SplashScreenVm(val view: ISplashScreenVIew) : ViewModel() {

    val localeProvider by kodein.instance<ILocaleProvider>()
    val context by kodein.instance<Context>()

    init {
        downloadData()
        initPreferences()
        Handler().postDelayed({
            view.startBaseApp()
        }, 3000)//FIXME delete delay
    }

    private fun initPreferences() {
        localeProvider.setUpLocale(context)
    }

    private fun downloadData() {
        //todo реализовать загрузку необходимых данных с учётом локали
    }
}