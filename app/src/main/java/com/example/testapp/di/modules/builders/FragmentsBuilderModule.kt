package com.example.testapp.di.modules.builders

import com.example.testapp.ui.cakes.cakesList.CakesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentsBuilderModule {

    @ContributesAndroidInjector
    fun contributeCakesFragment(): CakesFragment

}