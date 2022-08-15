package com.example.testapp.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseActivity<ViewModel : BaseViewModel, B : ViewDataBinding> : BaseActivityWithoutViewModel<B>() {

    private val viewModelClassCalculated: Class<ViewModel> = (
            javaClass
                    .genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<ViewModel>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewModel: ViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(viewModelClassCalculated)
    }

}