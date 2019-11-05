package com.simferopol.app.ui.routes.vm

import androidx.lifecycle.ViewModel
import com.simferopol.api.models.Route

class RouteVm(routeVm: Route) : ViewModel() {
    val imageUrl = routeVm.preview

}