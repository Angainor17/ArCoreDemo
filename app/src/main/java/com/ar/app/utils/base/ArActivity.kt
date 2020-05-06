package com.ar.app.utils.base

import androidx.appcompat.app.AppCompatActivity
import com.ar.app.ui.ar.showArCoreError

abstract class ArActivity : AppCompatActivity() {

    fun showUnsupportedError() {
        showArCoreError(this)
    }
}