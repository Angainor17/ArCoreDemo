package com.simferopol.app.utils.base

import androidx.appcompat.app.AppCompatActivity
import com.simferopol.app.ui.ar.showArCoreError

abstract class ArActivity : AppCompatActivity() {

    fun showUnsupportedError() {
        showArCoreError(this)
    }
}