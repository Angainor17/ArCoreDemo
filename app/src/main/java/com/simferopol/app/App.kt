package com.simferopol.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.simferopol.app.kodein.apiModule
import com.simferopol.app.kodein.providersModule
import com.simferopol.app.utils.storages.JsonDataStorage
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

const val YANDEX_MAP_API_KEY = "30d70067-9f77-4a49-b74d-35fe453e79a1"

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        kodein = Kodein {
            import(apiModule)
            import(providersModule)

            bind<Context>() with singleton { this@App }

            bind<JsonDataStorage>() with singleton {
                JsonDataStorage(instance())
            }
        }
    }

    companion object {
        lateinit var kodein: Kodein
    }
}