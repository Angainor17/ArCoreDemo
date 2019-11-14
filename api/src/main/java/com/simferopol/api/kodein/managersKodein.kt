package com.simferopol.api.kodein

import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.apiManager.ApiManagerImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val managersModule = Kodein.Module("ManagersModule") {

    bind<ApiManager>() with singleton { ApiManagerImpl(instance()) }
}