package com.simferopol.app.ui.arLocation.view

import android.location.Location
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import com.simferopol.app.R
import com.simferopol.app.ui.ar.isAndroidARSupported
import com.simferopol.app.ui.ar.isOpenGLSupported
import com.simferopol.app.ui.ar.showArCoreError
import com.simferopol.app.ui.arLocation.ARLocationVm
import com.simferopol.app.ui.arLocation.model.Geolocation
import com.simferopol.app.ui.arLocation.model.Venue
import com.simferopol.app.ui.arLocation.utils.AugmentedRealityLocationUtils
import com.simferopol.app.ui.arLocation.utils.INITIAL_MARKER_SCALE_MODIFIER
import com.simferopol.app.ui.arLocation.utils.INVALID_MARKER_SCALE_MODIFIER
import com.simferopol.app.ui.arLocation.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_ar_location.*
import kotlinx.android.synthetic.main.location_layout_renderable.view.*
import uk.co.appoly.arcorelocation.LocationMarker
import uk.co.appoly.arcorelocation.LocationScene
import java.lang.ref.WeakReference
import java.util.concurrent.CompletableFuture

const val VISIBLE_MAX_RANGE = 1000f

class ARLocationFragment : Fragment() {

    private lateinit var vm: ARLocationVm

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
        vm = ViewModelProviders.of(this).get(ARLocationVm::class.java)

        setupLoadingDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_ar_location, container, false)

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
        val alertDialogBuilder = AlertDialog.Builder(activity!!)
        val dialogHintMainView =
            LayoutInflater.from(activity!!).inflate(R.layout.loading_dialog, null) as LinearLayout
        alertDialogBuilder.setView(dialogHintMainView)
        loadingDialog = alertDialogBuilder.create()
        loadingDialog.setCanceledOnTouchOutside(false)
    }

    private fun setupSession() {
        if (arSceneView == null) return

        if (arSceneView.session == null) {
            try {
                val session =
                    AugmentedRealityLocationUtils.setupSession(activity!!, arCoreInstallRequested)

                if (session == null) {
                    arCoreInstallRequested = true
                    return
                } else {
                    arSceneView.setupSession(session)
                }
            } catch (e: Exception) {
                AugmentedRealityLocationUtils.handleSessionException(activity!!, e)
            }
        }

        try {
            if (locationScene == null) {
                locationScene = LocationScene(activity!!, arSceneView)
                locationScene?.setMinimalRefreshing(true)
                locationScene?.setOffsetOverlapping(true)
                locationScene?.anchorRefreshInterval = 2000
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            resumeArElementsTask.run()
        } catch (e: CameraNotAvailableException) {
            Toast.makeText(context, "Unable to get camera", Toast.LENGTH_LONG).show()
            activity?.onBackPressed()
            return
        }

        try {
            if (userGeolocation == Geolocation.EMPTY_GEOLOCATION) {
                LocationAsyncTask(WeakReference(this@ARLocationFragment)).execute(
                    locationScene!!
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fetchVenues(deviceLatitude: Double, deviceLongitude: Double) {
        loadingDialog.dismiss()
        userGeolocation = Geolocation(deviceLatitude.toString(), deviceLongitude.toString())
        setPoints()
    }

    private fun setPoints() {
        val venueList = vm.venuesLiveData.value

        venueList?.let {
            venuesSet.clear()
            venuesSet.addAll(it)
            areAllMarkersLoaded = false
            locationScene?.clearMarkers()
            renderVenues()
        }
    }

    private fun renderVenues() {
        setupAndRenderVenuesMarkers()
        updateVenuesMarkers()
    }

    private fun distanceToUser(venue: Venue): Float {
        val distance = FloatArray(2)

        Location.distanceBetween(
            userGeolocation.latitude?.toDouble() ?: 0.0,
            userGeolocation.longitude?.toDouble() ?: 0.0,
            venue.lat,
            venue.long,
            distance
        )
        return distance[0]
    }

    private fun setupAndRenderVenuesMarkers() {
        val venuesList = venuesSet.toList()
            .filter {
                distanceToUser(it) <= VISIBLE_MAX_RANGE
            }

        venuesList.forEach { venue ->
            val completableFutureViewRenderable = ViewRenderable.builder()
                .setView(context, R.layout.location_layout_renderable)
                .build()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return@forEach

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
                            if (venuesList.indexOf(venue) == venuesList.size - 1) {
                                areAllMarkersLoaded = true
                            }
                        }, 200)

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                    null
                }
        }
    }

    private fun updateVenuesMarkers() {
        arSceneView.scene.addOnUpdateListener {
            if (!areAllMarkersLoaded) return@addOnUpdateListener

            locationScene?.mLocationMarkers?.forEach { locationMarker ->
                locationMarker.height =
                    AugmentedRealityLocationUtils.generateRandomHeightBasedOnDistance(
                        locationMarker?.anchorNode?.distance ?: 0
                    )
            }

            val frame = arSceneView!!.arFrame ?: return@addOnUpdateListener
            if (frame.camera.trackingState != TrackingState.TRACKING) return@addOnUpdateListener

            locationScene?.processFrame(frame)
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
                AugmentedRealityLocationUtils.showDistance(activity!!, locationNode.distance)
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
        nodeLayout.setOnClickListener {
            val action = ARLocationFragmentDirections.actionNavArLocationToNavMonument(
                venue.geoObject
            )
            content.findNavController().navigate(action)
        }

        return node
    }

    private fun checkAndRequestPermissions() {
        if (activity != null && !isAndroidARSupported() || !isOpenGLSupported(activity!!)) {
            showArCoreError(activity!!)
            return
        }

        if (!PermissionUtils.hasLocationAndCameraPermissions(activity!!)) {
            PermissionUtils.requestCameraAndLocationPermissions(activity!!)
        } else {
            setupSession()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        if (!PermissionUtils.hasLocationAndCameraPermissions(activity!!)) {
            Toast.makeText(
                activity!!, R.string.camera_and_location_permission_request, Toast.LENGTH_LONG
            )
                .show()
            if (!PermissionUtils.shouldShowRequestPermissionRationale(activity!!)) {
                // Permission denied with checking "Do not ask again".
                PermissionUtils.launchPermissionSettings(activity!!)
            }
            activity?.onBackPressed()
        }
    }

    class LocationAsyncTask(private val activityWeakReference: WeakReference<ARLocationFragment>) :
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