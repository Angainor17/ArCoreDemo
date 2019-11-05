package com.simferopol.api.kodein

import com.simferopol.api.routeManager.RouteManager
import com.simferopol.api.routeManager.RouteManagerImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val managersModule = Kodein.Module("ManagersModule") {

    bind<RouteManager>() with singleton { RouteManagerImpl(instance()) }
}