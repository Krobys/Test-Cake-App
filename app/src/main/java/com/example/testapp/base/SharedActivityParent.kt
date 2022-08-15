package com.example.testapp.base

import androidx.lifecycle.ViewModelProvider

interface SharedActivityParent {

    val sharedViewModel: SharedViewModel

    fun getSharedViewModel(activity: BaseActivity<*, *>): SharedViewModel{
        return ViewModelProvider(activity, activity.viewModelFactory).get(SharedViewModel::class.java)
    }

    fun getSharedViewModel(activity: BaseActivityWithNavigator, viewModelFactory: ViewModelProvider.Factory): SharedViewModel{
        return ViewModelProvider(activity, viewModelFactory).get(SharedViewModel::class.java)
    }
}