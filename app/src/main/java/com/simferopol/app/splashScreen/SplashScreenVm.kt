package com.simferopol.app.splashScreen

import androidx.lifecycle.ViewModel

class SplashScreenVm(val view: ISplashScreenVIew) : ViewModel() {

    init {
        view.startBaseApp()
    }
}