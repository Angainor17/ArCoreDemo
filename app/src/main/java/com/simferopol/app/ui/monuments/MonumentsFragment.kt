package com.simferopol.app.ui.monuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.databinding.FragmentMonumentsBinding
import com.simferopol.app.ui.monuments.vm.MonumentListVm

class MonumentsFragment : Fragment() {

    private val monumentsVm = MonumentListVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMonumentsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = monumentsVm

        return binding.root
    }

}