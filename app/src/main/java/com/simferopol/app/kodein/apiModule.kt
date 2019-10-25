package com.simferopol.app.kodein

import com.simferopol.api.kodein.managersModule
import org.kodein.di.Kodein

internal val apiModule = Kodein.Module("ApiModule") {

    import(managersModule)
}