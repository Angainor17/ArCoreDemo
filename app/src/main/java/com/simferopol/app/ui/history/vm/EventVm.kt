package com.simferopol.app.ui.history.vm

import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModel
import com.simferopol.api.models.HistoricalEvent

class EventVm(eventVm: HistoricalEvent) : ViewModel() {

    val title = eventVm.title
    val subtitle = eventVm.subtitle
    val content = eventVm.content.parseAsHtml()
    val incut = eventVm.incut
}