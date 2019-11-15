package com.simferopol.app.ui.history.vm

import androidx.lifecycle.ViewModel
import com.simferopol.api.models.HistoricalEvent

class EventVm(eventVm: HistoricalEvent) : ViewModel() {

    val title = eventVm.title
    val subtitle = eventVm.subtitle
    val content = eventVm.content
    val incut = eventVm.incut
}