package com.simferopol.api.models

class Story(
    val id: Int,
    val name: String?,
    val preview: String?,
    val audio: String?,
    val events: ArrayList<HistoricalEvent>
)