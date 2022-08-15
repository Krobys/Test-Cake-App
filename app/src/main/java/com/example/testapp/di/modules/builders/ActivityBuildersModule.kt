package com.example.testapp.di.modules.builders

import android.app.Application
import com.example.testapp.TestApp
import com.example.testapp.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @Binds
    abstract fun bindTestApp(app: TestApp): Application

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

}