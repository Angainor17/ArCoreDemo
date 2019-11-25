package com.simferopol.app.ui.ar

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import com.simferopol.app.R
import com.simferopol.app.utils.SnackbarHelper
import java.io.IOException

private const val MIN_OPENGL_VERSION = 3.0
private const val IMAGE_FILE_NAME = "sampleImage.jpg"
private const val IMAGE_HIGH_METERS = 2f

/**
 * Extend the ArFragment to customize the ARCore session configuration to include Augmented Images.
 */
class AugmentedImageFragment : ArFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            SnackbarHelper().showError(
                activity!!,
                getString(R.string.sceneform_android_version_error)
            )
        }
        val openGlVersionString =
            (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
        if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
            SnackbarHelper()
                .showError(activity!!, getString(R.string.sceneform_opengl_version_error))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false
        return view
    }

    override fun getSessionConfiguration(session: Session): Config {
        val config = super.getSessionConfiguration(session)
        if (!setupAugmentedImageDatabase(config, session)) {
            SnackbarHelper().showError(activity!!, getString(R.string.image_database_error))
        }
        return config
    }

    private fun setupAugmentedImageDatabase(config: Config, session: Session): Boolean {
        val augmentedImageDatabase = AugmentedImageDatabase(session)
        var bitmap: Bitmap?
        try {
            context!!.assets.open(IMAGE_FILE_NAME)
                .use { inputStream ->
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    augmentedImageDatabase.addImage(
                        IMAGE_FILE_NAME,
                        bitmap,
                        IMAGE_HIGH_METERS
                    )
                }
        } catch (ignored: IOException) {
        }
        config.augmentedImageDatabase = augmentedImageDatabase
        return true
    }

}