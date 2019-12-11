package com.simferopol.app.ui.services.vm

import android.view.View
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.GeoObject
import com.simferopol.app.ui.services.ServiceFragmentDirections
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
    val audio = serviceVm.audio

    fun onClick(view: View) {
        val action = ServicesFragmentDirections.actionNavServicesToNavService(service)
        view.findNavController().navigate(action)
    }

    fun onItemMapClick(view: View) {
        val action = ServicesFragmentDirections.actionNavServicesToNavMap(service)
        view.findNavController().navigate(action)
    }

    fun onMapClick(view: View) {
        val action = ServiceFragmentDirections.actionNavServiceToNavMap(service)
        view.findNavController().navigate(action)
    }

    fun onPlayClick() {

    }
}