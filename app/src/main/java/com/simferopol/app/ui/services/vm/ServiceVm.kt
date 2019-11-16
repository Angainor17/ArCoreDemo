package com.simferopol.app.ui.services.vm

import androidx.lifecycle.ViewModel
import com.simferopol.api.models.Category

class ServiceVm(serviceVm: Category) : ViewModel() {

    val category = serviceVm
    val id = serviceVm.id
    val name = serviceVm.name
    val icon = serviceVm.icon
    val activeIcon = serviceVm.activeIcon
}