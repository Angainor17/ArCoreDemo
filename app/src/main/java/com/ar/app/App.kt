package com.ar.app

import android.content.Context
import androidx.multidex.MultiDexApplication

import com.ar.app.kodein.providersModule
import com.ar.app.utils.storages.JsonDataStorage
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        kodein = Kodein {
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