package com.example.testapp.di.modules

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object OtherModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideCicerone(): Cicerone<Router> {
        return Cicerone.create()
    }

}