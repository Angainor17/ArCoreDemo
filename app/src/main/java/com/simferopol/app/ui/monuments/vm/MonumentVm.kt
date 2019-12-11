package com.simferopol.app.ui.monuments.vm

import android.net.Uri
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
    val model = monumentVm.model
    val audio = monumentVm.audio

    fun onClick(view: View) {
        val action = MonumentsFragmentDirections.actionNavMonumentsToNavMonument(monument)
        view.findNavController().navigate(action)
    }

    fun onModelClick(view: View) {
        val file = "models/skeleton.stl"
        val fileUri = Uri.parse("assets://" + view.context.packageName + "/" + file).toString()
        val action = MonumentFragmentDirections.actionNavMonumentToNavModel3d(
            fileUri,
            "-1",
            "true"
        )
        view.findNavController().navigate(action)
    }

    fun onMapClick(view: View) {
        val action = MonumentFragmentDirections.actionNavMonumentToNavMap(monument)
        view.findNavController().navigate(action)
    }

    fun onPlayClick() {

    }
}