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

    val args: HistoryPagerFragmentArgs by navArgs()

  //  val startId = args.storyId// todo fix id passing from HistoryFragment
    val startId = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHistoryPagerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        historyListVm.id = startId
        binding.vm = historyListVm
        binding.pagerTab.setupWithViewPager(binding.storiesPager)
        return binding.root
    }
}