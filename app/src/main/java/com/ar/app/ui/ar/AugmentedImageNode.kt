package com.ar.app.ui.ar

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.ar.core.AugmentedImage
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import java.util.concurrent.CompletableFuture

private const val RENDER_3D_MODEL = "models/house.sfb"

/**
 * Node for rendering an augmented image. The image is framed by placing the virtual picture frame
 * at the corners of the augmented image trackable.
 */
class AugmentedImageNode(context: Context?) : AnchorNode() {

    private val house: CompletableFuture<ModelRenderable?> = ModelRenderable.builder()
        .setSource(context, Uri.parse(RENDER_3D_MODEL))
        .build()

    /**
     * Called when the AugmentedImage is detected and should be rendered. A Sceneform node tree is
     * created based on an Anchor created from the image. The corners are then positioned based on the
     * extents of the image. There is no need to worry about world coordinates since everything is
     * relative to the center of the image, which is the parent node of the corners.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun setImage(image: AugmentedImage) {
        if (!house.isDone) {
            CompletableFuture.allOf(house)
                .thenAccept { setImage(image) }
                .exceptionally { null }
        }
        anchor = image.createAnchor(image.centerPose)
        val localPosition = Vector3()
        val cornerNode = Node()
        localPosition[0f, 0.0f] = 0f
        cornerNode.setParent(this)
        cornerNode.localPosition = localPosition
        cornerNode.renderable = house.getNow(null)
    }
}