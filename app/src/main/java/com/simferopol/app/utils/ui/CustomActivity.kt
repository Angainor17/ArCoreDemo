package com.simferopol.app.utils.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simferopol.app.App
import com.simferopol.app.providers.lang.ILocaleProvider
import org.kodein.di.generic.instance

abstract class CustomActivity : AppCompatActivity() {

    private val localeProvider by App.kodein.instance<ILocaleProvider>()

    override fun onCreate(savedInstanceState: Bundle?) {
        localeProvider.setUpLocale(this)
        super.onCreate(savedInstanceState)
    }
}