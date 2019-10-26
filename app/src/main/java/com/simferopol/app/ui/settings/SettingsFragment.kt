package com.simferopol.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.databinding.FragmentSettingsBinding
import com.simferopol.app.ui.services.ServiceVm

class SettingsFragment : Fragment() {

    private val settingsVm = SettingsVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = settingsVm

        return binding.root
    }
}