package com.example.testapp

import com.example.testapp.di.components.DaggerAppComponent
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class TestApp : DaggerApplication() {

    @Inject
    lateinit var cicerone: Cicerone<Router>
    private val navigatorHolder get() = cicerone.getNavigatorHolder()
    private var lastNavigator: AppNavigator? = null

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    fun setNavigatorToHolder(navigator: AppNavigator) {
        lastNavigator = navigator
        navigatorHolder.setNavigator(navigator)
    }

    fun removeNavigator() {
        navigatorHolder.removeNavigator()
    }

    fun getLastNavigator(): AppNavigator? {
        return lastNavigator
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                val APP_TAG = "TEST_APP"
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, "${APP_TAG}_$tag", message, t)
                }
            })
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().bindApplication(this).build()
    }

}