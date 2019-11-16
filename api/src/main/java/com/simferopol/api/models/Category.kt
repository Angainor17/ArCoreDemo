package com.simferopol.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Category(
    val id: Int,
    val name: String?,
    val icon: String?,
    @SerializedName("active_icon") val activeIcon: String?
) : Serializable