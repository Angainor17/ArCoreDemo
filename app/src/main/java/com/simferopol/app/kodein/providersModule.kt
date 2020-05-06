package com.simferopol.app.kodein

import android.content.Context
import com.simferopol.app.providers.res.IResProvider
import com.simferopol.app.providers.res.ResProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

internal val providersModule = Kodein.Module("ProvidersModule") {

    bind<IResProvider>() with singleton { ResProvider(instance<Context>().resources) }

}
