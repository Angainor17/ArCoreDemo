package com.simferopol.app.ui.monuments.vm

import android.util.Log
import android.view.View
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.GeoObject
import com.simferopol.app.ui.monuments.MonumentFragmentDirections
import com.simferopol.app.ui.monuments.MonumentsFragmentDirections

class MonumentVm(monumentVm: GeoObject) : ViewModel() {

    private val monument = monumentVm
    val imageUrl = monumentVm.preview
    val name = monumentVm.name
    val content = monumentVm.content?.parseAsHtml()

    fun onClick(view: View) {
        val action = MonumentsFragmentDirections.actionNavMonumentsToNavMonument(monument)
        view.findNavController().navigate(action)
    }

    fun onModelClick() {
       Log.e("3dButton", name)// todo navigate to 3d model screen
    }

    fun onMapClick(view: View) {
        val action = MonumentFragmentDirections.actionNavMonumentToNavObjectMap(monument)
        view.findNavController().navigate(action)
    }

    fun onPlayClick() {
        Log.e("play", name)// todo play audio file
    }
}