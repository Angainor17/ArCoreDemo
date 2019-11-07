package com.simferopol.api.models

class GeoObject(
    val id: Int,
    val name: String?,
    val category_id: Int?,
    val lat: Float?,
    val lon: Float?,
    val address: String?,
    val about: String?,
    val content: String?,
    val icon: String?,
    val active_icon: String?,
    val activeIcon: String?,
    val audio: String?,
    val format: String?,
    val worktime: String?,
    val phone: String?,
    val website: String?,
    val ticket_price: String?,
    val average_price: String?,
    val payment_method: String?
)
