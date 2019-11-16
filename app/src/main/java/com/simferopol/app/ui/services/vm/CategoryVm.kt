package com.simferopol.app.ui.services.vm

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.Category
import com.simferopol.app.ui.services.CategoryFragmentDirections

class CategoryVm(serviceVm: Category) : ViewModel() {

    val category = serviceVm
    val id = serviceVm.id
    val name = serviceVm.name
    val icon = serviceVm.icon
    val activeIcon = serviceVm.activeIcon

    fun onClick(view: View) {
        val action = CategoryFragmentDirections.actionNavCategoryToNavServices(category)
        view.findNavController().navigate(action)
    }
}