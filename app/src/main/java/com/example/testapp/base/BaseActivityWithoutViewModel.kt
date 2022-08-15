package com.example.testapp.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.testapp.R
import com.example.testapp.TestApp
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


abstract class BaseActivityWithoutViewModel<B : ViewDataBinding>() : DaggerAppCompatActivity() {

    @Inject
    protected lateinit var topyApp: TestApp

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected open val layoutId: Int = R.layout.activity_base_container

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        initViews()
    }

    open fun initViews(){

    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onBackPressed() {
        var backPressedListener: OnBackPressedFragmentsListener? = null
        supportFragmentManager.findFragmentById(R.id.fragment_container)?.childFragmentManager?.primaryNavigationFragment?.let {
            if (it is OnBackPressedFragmentsListener) {
                backPressedListener = it
            }
        }
        val isBackPressedOnFragment = backPressedListener?.onBackPressed() ?: false

        if (!isBackPressedOnFragment){
            super.onBackPressed()
        }
    }
}