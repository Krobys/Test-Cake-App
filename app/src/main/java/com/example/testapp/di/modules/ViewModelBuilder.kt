package com.example.testapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testapp.base.SharedViewModel
import com.example.testapp.di.vmfactory.ViewModelFactory
import com.example.testapp.di.vmfactory.ViewModelKey
import com.example.testapp.ui.cakes.cakesList.CakesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelBuilder {
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel::class)
    fun bindSharedViewModel(viewModel: SharedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CakesViewModel::class)
    fun bindCakesViewModel(viewModel: CakesViewModel): ViewModel
}