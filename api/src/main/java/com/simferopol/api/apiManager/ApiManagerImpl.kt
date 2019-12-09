package com.simferopol.api.apiManager

import android.content.Context
import com.google.gson.Gson
import com.simferopol.api.models.*
import com.simferopol.api.retrofit.createRetrofit
import com.simferopol.api.utils.*
import java.util.*
import kotlin.collections.ArrayList

private const val WEATHER_BASE_URL = "https://api.openweathermap.org"
private const val WEATHER_KEY = "efe6c478c7c1859c4d922f6cf57f07d6"

private const val SIMFER_LAT = 44.958009f
private const val SIMFER_LON = 34.106231f

private const val WEATHER_PREF_KEY = "weather"
private const val DATE_PREF_KEY = "date"

private const val TEMPERATURE_EXPIRED = 12 * 60 * 60 * 1000L

internal class ApiManagerImpl(private val context: Context) : ApiManager {

    private val weatherApi = createRetrofit(WEATHER_BASE_URL).create(WeatherApi::class.java)
    private val prefs = JsonDataStorage(context)
    private val gson = Gson()

    override suspend fun getWeather(): ManagerResult<Weather> {
        return wrapManagerResult {
            val lastWeatherDate = try {
                gson.fromJson<Date>(prefs.getJson(DATE_PREF_KEY) ?: "")
            } catch (e: Exception) {
                Date(Date().time - 2 * TEMPERATURE_EXPIRED)
            }

            val expiredDate = Date(Date().time - TEMPERATURE_EXPIRED)

            if (expiredDate.before(lastWeatherDate)) {
                return@wrapManagerResult gson.fromJson<Weather>(
                    prefs.getJson(WEATHER_PREF_KEY) ?: ""
                )
            }

            val res = weatherApi.getWeather(SIMFER_LAT, SIMFER_LON, WEATHER_KEY)
            prefs.put(WEATHER_PREF_KEY, res)
            prefs.put(DATE_PREF_KEY, Date())

            return@wrapManagerResult res
        }
    }

    override suspend fun getRoutes(): ManagerResult<ArrayList<Route>> {
        return wrapManagerResult {
            Gson().fromJson<ArrayList<Route>>(
                readJson(
                    context,
                    "routes.json"
                )
            )
        }
    }

    override suspend fun getRoute(): ManagerResult<Route> {
        return wrapManagerResult {
            Gson().fromJson<Route>(
                readJson(
                    context,
                    "route.json"
                )
            )
        }
    }

    override suspend fun getGeoObjects(categoryId: Int?): ManagerResult<ArrayList<GeoObject>> {
        val list = Gson().fromJson<ArrayList<GeoObject>>(
            readJson(context, "geoObjects.json")
        )
        return wrapManagerResult {
            if (categoryId != null)
                ArrayList(list.filter { geoObject -> geoObject.categoryId == categoryId })
            else list
        }
    }

    override suspend fun getAbout(): ManagerResult<AboutCityInfo> {
        return wrapManagerResult {
            Gson().fromJson<AboutCityInfo>(
                readJson(
                    context,
                    "about.json"
                )
            )
        }
    }

    override suspend fun getStories(): ManagerResult<ArrayList<Story>> {
        return wrapManagerResult {
            Gson().fromJson<ArrayList<Story>>(
                readJson(
                    context,
                    "stories.json"
                )
            )
        }
    }

    override suspend fun getCategories(): ManagerResult<ArrayList<Category>> {
        return wrapManagerResult {
            Gson().fromJson<ArrayList<Category>>(
                readJson(
                    context,
                    "categories.json"
                )
            )
        }
    }
}