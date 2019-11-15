package com.simferopol.app.ui.history.vm

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.Story
import com.simferopol.app.ui.history.HistoryFragmentDirections

class HistoryVm(historyVm: Story) : ViewModel() {

    val id = historyVm.id
    val name = historyVm.name
    val preview = historyVm.preview
    val audio = historyVm.audio
    val events = historyVm.events
    val eventList = (ArrayList(events.map { EventVm(it) } ?: ArrayList()))


    fun onClick(view: View) {
        val action = HistoryFragmentDirections.actionNavHistoryToNavHistoryPager(storyId = id)
        view.findNavController().navigate(action)
    }

    fun onPlayClick() {
        Log.e("play", name)// todo play audio file
    }
}