package com.simferopol.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.databinding.FragmentHistoryBinding
import com.simferopol.app.ui.history.vm.HistoryListVm

class HistoryFragment : Fragment() {

    private val historyListVm = HistoryListVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = historyListVm

        return binding.root
    }
}