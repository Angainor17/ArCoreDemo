package com.simferopol.app.kodein

import android.content.Context
import com.simferopol.app.providers.audio.AudioProvider
import com.simferopol.app.providers.audio.IAudioProvider
import com.simferopol.app.providers.lang.ILocaleProvider
import com.simferopol.app.providers.lang.LocaleProvider
import com.simferopol.app.providers.res.IResProvider
import com.simferopol.app.providers.res.ResProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

internal val providersModule = Kodein.Module("ProvidersModule") {

    bind<IResProvider>() with singleton { ResProvider(instance<Context>().resources) }
    bind<ILocaleProvider>() with singleton { LocaleProvider() }
    bind<IAudioProvider>() with singleton { AudioProvider(instance()) }

}
