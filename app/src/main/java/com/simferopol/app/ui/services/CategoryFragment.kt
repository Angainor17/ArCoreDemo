package com.simferopol.app.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.databinding.FragmentCategoriesBinding
import com.simferopol.app.ui.services.vm.CategoriesListVm

class CategoryFragment : Fragment() {

    private val serviceVm = CategoriesListVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = serviceVm

        return binding.root
    }
}