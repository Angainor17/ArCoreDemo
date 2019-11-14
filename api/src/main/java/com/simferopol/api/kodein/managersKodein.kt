package com.simferopol.api.kodein

import com.simferopol.api.dataManager.DataManager
import com.simferopol.api.dataManager.DataManagerImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val managersModule = Kodein.Module("ManagersModule") {

    bind<DataManager>() with singleton { DataManagerImpl(instance()) }
}