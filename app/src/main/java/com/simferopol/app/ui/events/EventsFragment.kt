package com.simferopol.app.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.databinding.FragmentEventsBinding

class EventsFragment : Fragment() {

    private val eventsVm = EventsVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = eventsVm

        return binding.root
    }
}