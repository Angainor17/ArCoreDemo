package com.simferopol.app.ui.infoMarks

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.zxing.Result
import com.simferopol.app.App
import org.kodein.di.generic.instance

abstract class QrCodeVM : ViewModel() {

    var pauseAction: (() -> Unit)? = null
    var resumeAction: (() -> Unit)? = null

    val context by App.kodein.instance<Context>()

    abstract fun scannerResult(result: Result)

    abstract fun errorCallback(exception: Exception)

    fun checkPermission(activity: Activity) {
        if (hasCameraPermission()) {
            requestPermission(activity)
        }
    }

    private fun requestPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_DENIED
}