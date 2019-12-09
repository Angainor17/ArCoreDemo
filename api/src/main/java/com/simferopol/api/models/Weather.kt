package com.simferopol.api.models

import com.google.gson.annotations.SerializedName

class Weather(
    private val main: Main,
    private val weather: List<WeatherItem>
) {

    fun getTemperature() = main.temp - 273f

    fun getIconName(): String = weather.getOrNull(0)?.icon ?: ""

    class Main(
        @SerializedName("temp") val temp: Float,
        @SerializedName("pressure") val pressure: Float,
        @SerializedName("humidity") val humidity: Float,
        @SerializedName("temp_min") val tempMin: Float,
        @SerializedName("temp_max") val tempMax: Float
    )

    class WeatherItem(
        @SerializedName("icon") val icon: String
    )
}