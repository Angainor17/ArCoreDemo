package com.simferopol.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GeoObject(
    val id: Int,
    val name: String?,
    @SerializedName("category_id") val categoryId: Int?,
    val lat: Float?,
    val lon: Float?,
    val preview: String?,
    val address: String?,
    val about: String?,
    val content: String?,
    val icon: String?,
    @SerializedName("active_icon")val activeIcon: String?,
    val audio: String?,
    val slides: ArrayList<String>?,
    val format: String?,
    val worktime: String?,
    val phone: String?,
    val website: String?,
    @SerializedName("ticket_price")val ticketPrice: String?,
    @SerializedName("average_price")val averagePrice: String?,
    @SerializedName("payment_method")val paymentMethod: String?
): Serializable
