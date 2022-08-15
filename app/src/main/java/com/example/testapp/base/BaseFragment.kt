package com.example.testapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType
import javax.inject.Inject


abstract class BaseFragment<
        ViewModel : BaseViewModel,
        DataBinding : ViewDataBinding>
    : BaseFragmentWithoutViewModel<DataBinding>() {

    private val viewModelClassCalculated: Class<ViewModel> = (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<ViewModel>

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewModel: ViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(viewModelClassCalculated)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        observeViewModel()
        return binding.root
    }

    abstract fun observeViewModel()

}