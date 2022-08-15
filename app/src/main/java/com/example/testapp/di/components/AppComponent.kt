package com.example.testapp.di.components

import com.example.testapp.TestApp
import com.example.testapp.di.modules.*
import com.example.testapp.di.modules.builders.ActivityBuildersModule
import com.example.testapp.di.modules.builders.FragmentsBuilderModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        ActivityBuildersModule::class,
        FragmentsBuilderModule::class,
        ViewModelBuilder::class,
        OtherModule::class]
)
interface AppComponent : AndroidInjector<TestApp>{

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindApplication(app: TestApp): Builder

        fun build(): AppComponent
    }

    override fun inject(app: TestApp)

}