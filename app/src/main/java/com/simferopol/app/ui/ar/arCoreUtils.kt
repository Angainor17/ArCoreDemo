package com.simferopol.app.ui.ar

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.simferopol.app.R
import com.simferopol.app.utils.SnackbarHelper

fun showArCoreError(activity: Activity) {
    if (!isAndroidARSupported()) showAndroidArUnsupportedError(activity)

    if (!isOpenGLSupported(activity)) showOpenGlUnsupportedError(activity)
}

private fun showOpenGlUnsupportedError(activity: Activity) {
    SnackbarHelper().showError(
        activity,
        activity.getString(R.string.sceneform_opengl_version_error)
    )
}

fun isAndroidARSupported() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

fun isOpenGLSupported(activity: Activity): Boolean {
    val openGlVersionString =
        (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo
            .glEsVersion
    return openGlVersionString.toDouble() > MIN_OPENGL_VERSION
}

fun showAndroidArUnsupportedError(activity: Activity) {
    SnackbarHelper().showError(
        activity,
        activity.getString(R.string.sceneform_android_version_error)
    )
}

