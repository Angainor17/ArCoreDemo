package com.simferopol.app

import android.app.Application
import android.content.Context
import com.simferopol.app.kodein.apiModule
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        kodein = Kodein {
            import(apiModule)

            bind<Context>() with singleton { this@App }
        }
    }

    companion object {
        lateinit var kodein: Kodein
    }
}