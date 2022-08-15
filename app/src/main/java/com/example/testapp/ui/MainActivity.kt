package com.example.testapp.ui

import com.example.testapp.base.BaseActivityWithNavigator
import com.example.testapp.base.BaseFragmentWithoutViewModel
import com.example.testapp.base.SharedActivityParent
import com.example.testapp.base.SharedViewModel
import com.example.testapp.di.vmfactory.ViewModelFactory
import com.example.testapp.tools.asScreenAnimated
import com.example.testapp.ui.cakes.cakesList.CakesFragment
import com.jaeger.library.StatusBarUtil
import javax.inject.Inject

class MainActivity() : BaseActivityWithNavigator(), SharedActivityParent {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    override val sharedViewModel: SharedViewModel
        get() = getSharedViewModel(this, viewModelFactory)

    override fun initViews() {
        StatusBarUtil.setTransparent(this)
        router.newRootScreen(
            CakesFragment::class.java.asScreenAnimated(
                BaseFragmentWithoutViewModel.TransitionAnimation.NO_ANIMATION
            )
        )
    }


}
