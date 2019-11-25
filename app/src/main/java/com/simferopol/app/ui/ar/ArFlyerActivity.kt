package com.simferopol.app.ui.ar

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.ux.ArFragment
import com.simferopol.app.R
import kotlinx.android.synthetic.main.activity_ar_flyer.*
import java.util.*

class ArFlyerActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment

    private val augmentedImageMap: MutableMap<AugmentedImage, AugmentedImageNode?> = HashMap()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_flyer)
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

        arFragment.arSceneView.scene.addOnUpdateListener { frameTime: FrameTime ->
            onUpdateFrame(
                frameTime
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (augmentedImageMap.isEmpty()) {
            fitToScanView!!.visibility = View.VISIBLE
        }
    }

    /**
     * Registered with the Sceneform Scene object, this method is called at the start of each frame.
     *
     * @param frameTime - time since last frame.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun onUpdateFrame(frameTime: FrameTime) {
        val frame = arFragment.arSceneView.arFrame ?: return
        val updatedAugmentedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)
        for (augmentedImage in updatedAugmentedImages) {
            when (augmentedImage.trackingState) {
                TrackingState.TRACKING -> {
                    fitToScanView!!.visibility = View.GONE
                    if (!augmentedImageMap.containsKey(augmentedImage)) {
                        val node = AugmentedImageNode(this)
                        node.setImage(augmentedImage)
                        augmentedImageMap[augmentedImage] = node
                        arFragment
                            .arSceneView.scene.addChild(node)
                    }
                }
                TrackingState.STOPPED -> augmentedImageMap.remove(augmentedImage)
            }
        }
    }
}