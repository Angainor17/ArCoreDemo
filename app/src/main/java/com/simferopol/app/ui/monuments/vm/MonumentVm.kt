package com.simferopol.app.ui.monuments.vm

import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.GeoObject
import com.simferopol.api.models.Route
import com.simferopol.app.ui.routes.RoutesFragmentDirections

class MonumentVm(monumentVm: GeoObject) : ViewModel() {

    val monumentId = monumentVm.id
    val imageUrl = monumentVm.preview
    val name = monumentVm.name


    fun onItemTouch(view: View, motionEvent: MotionEvent):Boolean {
        when (motionEvent.action){
            MotionEvent.ACTION_DOWN -> view.alpha = 0.9f
            MotionEvent.ACTION_CANCEL -> view.alpha = 0.3f
            MotionEvent.ACTION_UP -> {
                view.alpha = 0.3f
                onClick(view)
            }
        }
        return  true
    }

    private fun onClick(view: View){
      //  val action = RoutesFragmentDirections.actionNavRoutesToNavRoute(routeId,name,imageUrl,distance,time)
       // view.findNavController().navigate(action)
    }
}