package com.simferopol.app.ui.services.vm

import android.util.Log
import android.view.View
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.GeoObject
import com.simferopol.app.ui.services.ServicesFragmentDirections

class ServiceVm(serviceVm: GeoObject) : ViewModel() {

    private val service = serviceVm
    val imageUrl = serviceVm.preview
    val name = serviceVm.name
    val content = serviceVm.content?.parseAsHtml()
    val address = serviceVm.address
    val contacts = serviceVm.phone
    val site = serviceVm.website
    val worktime = serviceVm.worktime
    val slides = serviceVm.slides

    fun onClick(view: View) {
        val action = ServicesFragmentDirections.actionNavServicesToNavService(service)
        view.findNavController().navigate(action)
    }

    fun onMapClick() {
        Log.e("map", name)// todo navigate to map
    }

    fun onPlayClick() {
        Log.e("play", name)// todo play audio
    }
}