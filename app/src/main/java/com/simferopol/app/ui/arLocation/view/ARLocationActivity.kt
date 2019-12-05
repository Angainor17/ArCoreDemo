package com.simferopol.app.ui.arLocation.view

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import com.simferopol.app.R
import com.simferopol.app.ui.ar.isAndroidARSupported
import com.simferopol.app.ui.ar.isOpenGLSupported
import com.simferopol.app.ui.arLocation.model.Geolocation
import com.simferopol.app.ui.arLocation.model.Venue
import com.simferopol.app.ui.arLocation.utils.AugmentedRealityLocationUtils
import com.simferopol.app.ui.arLocation.utils.AugmentedRealityLocationUtils.INITIAL_MARKER_SCALE_MODIFIER
import com.simferopol.app.ui.arLocation.utils.AugmentedRealityLocationUtils.INVALID_MARKER_SCALE_MODIFIER
import com.simferopol.app.ui.arLocation.utils.PermissionUtils
import com.simferopol.app.utils.base.ArActivity
import kotlinx.android.synthetic.main.activity_ar_location.*
import kotlinx.android.synthetic.main.location_layout_renderable.view.*
import uk.co.appoly.arcorelocation.LocationMarker
import uk.co.appoly.arcorelocation.LocationScene
import java.lang.ref.WeakReference
import java.util.concurrent.CompletableFuture

class ARLocationActivity : ArActivity() {

    private var arCoreInstallRequested = false
    private var locationScene: LocationScene? = null

    private lateinit var loadingDialog: AlertDialog

    private val resumeArElementsTask = Runnable {
        locationScene?.resume()
        arSceneView.resume()
    }

    private var userGeolocation = Geolocation.EMPTY_GEOLOCATION
    private var venuesSet: MutableSet<Venue> = mutableSetOf()
    private var areAllMarkersLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_location)
        setupLoadingDialog()
    }

    override fun onResume() {
        super.onResume()
        checkAndRequestPermissions()
    }

    override fun onPause() {
        super.onPause()
        arSceneView.session?.let {
            locationScene?.pause()
            arSceneView?.pause()
        }
    }

    private fun setupLoadingDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val dialogHintMainView =
            LayoutInflater.from(this).inflate(R.layout.loading_dialog, null) as LinearLayout
        alertDialogBuilder.setView(dialogHintMainView)
        loadingDialog = alertDialogBuilder.create()
        loadingDialog.setCanceledOnTouchOutside(false)
    }

    private fun setupSession() {
        if (arSceneView == null) return

        if (arSceneView.session == null) {
            try {
                val session =
                    AugmentedRealityLocationUtils.setupSession(this, arCoreInstallRequested)
                if (session == null) {
                    arCoreInstallRequested = true
                    return
                } else {
                    arSceneView.setupSession(session)
                }
            } catch (e: UnavailableException) {
                AugmentedRealityLocationUtils.handleSessionException(this, e)
            }
        }

        if (locationScene == null) {
            locationScene = LocationScene(this, arSceneView)
            locationScene?.setMinimalRefreshing(true)
            locationScene?.setOffsetOverlapping(true)
            locationScene?.anchorRefreshInterval = 2000
        }

        try {
            resumeArElementsTask.run()
        } catch (e: CameraNotAvailableException) {
            Toast.makeText(this, "Unable to get camera", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (userGeolocation == Geolocation.EMPTY_GEOLOCATION) {
            LocationAsyncTask(WeakReference(this@ARLocationActivity)).execute(
                locationScene!!
            )
        }
    }

    private fun fetchVenues(deviceLatitude: Double, deviceLongitude: Double) {
        loadingDialog.dismiss()
        userGeolocation = Geolocation(deviceLatitude.toString(), deviceLongitude.toString())
        setPoints()
    }

    private fun setPoints() {
        val venueList = arrayListOf(//FIXME test values
            Venue("центр", "avenue центр", 44.590836, 33.511868, ""),
            Venue("вверх", "avenu2e вверх", 44.592779, 33.513789, ""),
            Venue("низ", "avenu2e низ", 44.588130, 33.510529, ""),
            Venue("право", "avenu2e право", 44.588582, 33.516881, ""),
            Venue("Лево", "avenue Лево", 44.592768, 33.508040, "")
        )

        venuesSet.clear()
        venuesSet.addAll(venueList)
        areAllMarkersLoaded = false
        locationScene!!.clearMarkers()
        renderVenues()
    }

    private fun renderVenues() {
        setupAndRenderVenuesMarkers()
        updateVenuesMarkers()
    }

    private fun setupAndRenderVenuesMarkers() {
        venuesSet.forEach { venue ->
            val completableFutureViewRenderable = ViewRenderable.builder()
                .setView(this, R.layout.location_layout_renderable)
                .build()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                CompletableFuture.anyOf(completableFutureViewRenderable)
                    .handle<Any> { _, throwable ->
                        if (throwable != null) return@handle null
                        try {
                            val venueMarker = LocationMarker(
                                venue.long,
                                venue.lat,
                                setVenueNode(venue, completableFutureViewRenderable)
                            )
                            Handler(Looper.getMainLooper()).postDelayed({
                                attachMarkerToScene(
                                    venueMarker,
                                    completableFutureViewRenderable.get().view
                                )
                                if (venuesSet.indexOf(venue) == venuesSet.size - 1) {
                                    areAllMarkersLoaded = true
                                }
                            }, 200)

                        } catch (ex: Exception) {
                            //                        showToast(getString(R.string.generic_error_msg))
                        }
                        null
                    }
            }
        }
    }

    private fun updateVenuesMarkers() {
        arSceneView.scene.addOnUpdateListener() {
            if (!areAllMarkersLoaded) {
                return@addOnUpdateListener
            }

            locationScene?.mLocationMarkers?.forEach { locationMarker ->
                locationMarker.height =
                    AugmentedRealityLocationUtils.generateRandomHeightBasedOnDistance(
                        locationMarker?.anchorNode?.distance ?: 0
                    )
            }


            val frame = arSceneView!!.arFrame ?: return@addOnUpdateListener
            if (frame.camera.trackingState != TrackingState.TRACKING) {
                return@addOnUpdateListener
            }
            locationScene!!.processFrame(frame)
        }
    }

    private fun attachMarkerToScene(locationMarker: LocationMarker, layoutRendarable: View) {
        resumeArElementsTask.run {
            locationMarker.scalingMode = LocationMarker.ScalingMode.FIXED_SIZE_ON_SCREEN
            locationMarker.scaleModifier = INITIAL_MARKER_SCALE_MODIFIER

            locationScene?.mLocationMarkers?.add(locationMarker)
            locationMarker.anchorNode?.isEnabled = true

            Handler(Looper.getMainLooper()).post {
                locationScene?.refreshAnchors()
                layoutRendarable.pinContainer.visibility = View.VISIBLE
            }
        }
        locationMarker.setRenderEvent { locationNode ->
            layoutRendarable.distance.text =
                AugmentedRealityLocationUtils.showDistance(locationNode.distance)
            resumeArElementsTask.run {
                computeNewScaleModifierBasedOnDistance(locationMarker, locationNode.distance)
            }
        }
    }

    private fun computeNewScaleModifierBasedOnDistance(
        locationMarker: LocationMarker,
        distance: Int
    ) {
        val scaleModifier =
            AugmentedRealityLocationUtils.getScaleModifierBasedOnRealDistance(distance)
        return if (scaleModifier == INVALID_MARKER_SCALE_MODIFIER) {
            detachMarker(locationMarker)
        } else {
            locationMarker.scaleModifier = scaleModifier
        }
    }

    private fun detachMarker(locationMarker: LocationMarker) {
        locationMarker.anchorNode?.anchor?.detach()
        locationMarker.anchorNode?.isEnabled = false
        locationMarker.anchorNode = null
    }

    private fun setVenueNode(
        venue: Venue,
        completableFuture: CompletableFuture<ViewRenderable>
    ): Node {
        val node = Node()
        node.renderable = completableFuture.get()

        val nodeLayout = completableFuture.get().view
        val venueName = nodeLayout.name
        val markerLayoutContainer = nodeLayout.pinContainer
        venueName.text = venue.name
        markerLayoutContainer.visibility = View.GONE
        nodeLayout.setOnTouchListener { _, _ ->
            Toast.makeText(this, venue.address, Toast.LENGTH_SHORT).show()
            false
        }

        return node
    }

    private fun checkAndRequestPermissions() {
        if (!isAndroidARSupported() || !isOpenGLSupported(this)) {
            showUnsupportedError()
            return
        }

        if (!PermissionUtils.hasLocationAndCameraPermissions(this)) {
            PermissionUtils.requestCameraAndLocationPermissions(this)
        } else {
            setupSession()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        if (!PermissionUtils.hasLocationAndCameraPermissions(this)) {
            Toast.makeText(
                this, R.string.camera_and_location_permission_request, Toast.LENGTH_LONG
            )
                .show()
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                PermissionUtils.launchPermissionSettings(this)
            }
            finish()
        }
    }

    class LocationAsyncTask(private val activityWeakReference: WeakReference<ARLocationActivity>) :
        AsyncTask<LocationScene, Void, List<Double>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            activityWeakReference.get()!!.loadingDialog.show()
        }

        override fun doInBackground(vararg p0: LocationScene): List<Double> {
            var deviceLatitude: Double?
            var deviceLongitude: Double?
            do {
                deviceLatitude = p0[0].deviceLocation?.currentBestLocation?.latitude
                deviceLongitude = p0[0].deviceLocation?.currentBestLocation?.longitude
            } while (deviceLatitude == null || deviceLongitude == null)
            return listOf(deviceLatitude, deviceLongitude)
        }

        override fun onPostExecute(geolocation: List<Double>) {
            activityWeakReference.get()!!.fetchVenues(
                deviceLatitude = geolocation[0],
                deviceLongitude = geolocation[1]
            )
            super.onPostExecute(geolocation)
        }
    }
}