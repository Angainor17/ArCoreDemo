package com.simferopol.app.ui.ar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

const val MIN_OPENGL_VERSION = 3.0
private const val IMAGE_FILE_NAME = "sampleImage.jpg"
private const val IMAGE_HIGH_METERS = 2f

/**
 * Extend the ArFragment to customize the ARCore session configuration to include Augmented Images.
 */
class AugmentedImageFragment : ArFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            if (!isAndroidARSupported() || !isOpenGLSupported(it)) {
                showArCoreError(it)
            }
        }
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