package com.simferopol.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.app.databinding.FragmentHistoryPagerBinding
import com.simferopol.app.ui.history.vm.HistoryListVm

class HistoryPagerFragment : Fragment() {

    private val historyListVm = HistoryListVm()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: HistoryPagerFragmentArgs by navArgs()
        var startId = args.storyId
        val binding = FragmentHistoryPagerBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        historyListVm.id = startId
        binding.vm = historyListVm
        return binding.root
    }
}