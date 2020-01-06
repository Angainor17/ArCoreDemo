package com.simferopol.app.utils

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import com.simferopol.app.R

private const val BACKGROUND_COLOR = -0x40cdcdce

/**
 * Helper to manage the sample snackbar. Hides the Android boilerplate code, and exposes simpler
 * methods.
 */
class SnackbarHelper {

    private var messageSnackbar: Snackbar? = null

    private enum class DismissBehavior {
        HIDE, SHOW, FINISH
    }

    fun isShowing(): Boolean = messageSnackbar != null

    /**
     * Shows a snackbar with a given message.
     */
    fun showMessage(activity: Activity, message: String) {
        show(activity, message, DismissBehavior.HIDE)
    }

    /**
     * Shows a snackbar with a given message, and a dismiss button.
     */
    fun showMessageWithDismiss(activity: Activity, message: String) {
        show(activity, message, DismissBehavior.SHOW)
    }

    /**
     * Shows a snackbar with a given error message. When dismissed, will finish the activity. Useful
     * for notifying errors, where no further interaction with the activity is possible.
     */
    fun showError(activity: Activity, errorMessage: String) {
        show(activity, errorMessage, DismissBehavior.FINISH)
    }

    /**
     * Hides the currently showing snackbar, if there is one. Safe to call from any thread. Safe to
     * call even if snackbar is not shown.
     */
    fun hide(activity: Activity) {
        activity.runOnUiThread {
            messageSnackbar?.dismiss()
            messageSnackbar = null
        }
    }

    private fun show(activity: Activity, message: String, dismissBehavior: DismissBehavior) {
        activity.runOnUiThread {
            try {
                messageSnackbar = Snackbar.make(
                    activity.findViewById(R.id.content),
                    message,
                    Snackbar.LENGTH_INDEFINITE
                )

                messageSnackbar?.view?.setBackgroundColor(BACKGROUND_COLOR)

                if (dismissBehavior != DismissBehavior.HIDE) {
                    messageSnackbar?.setAction(activity.getString(R.string.dismiss)) {
                        messageSnackbar?.dismiss()
                        activity.onBackPressed()
                    }
                }
                messageSnackbar?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}