package com.simferopol.app.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private val aboutVm = AboutVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = aboutVm

        return binding.root
    }
}