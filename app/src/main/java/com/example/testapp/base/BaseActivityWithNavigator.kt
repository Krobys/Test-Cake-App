package com.example.testapp.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.testapp.R
import com.example.testapp.TestApp
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import com.example.testapp.tools.hideKeyboard
import com.example.testapp.tools.setUpTransactionWithAnimation
import javax.inject.Inject


abstract class BaseActivityWithNavigator() : DaggerAppCompatActivity() {

    @Inject
    lateinit var testApp: TestApp

    val router get() = testApp.cicerone.router

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected open val layoutId: Int = R.layout.activity_base_container

    protected open val navigator: AppNavigator by lazy {
        object : AppNavigator(this, R.id.fragment_container) {

            override fun setupFragmentTransaction(
                screen: FragmentScreen,
                fragmentTransaction: FragmentTransaction,
                currentFragment: Fragment?,
                nextFragment: Fragment
            ) {
                this@BaseActivityWithNavigator.hideKeyboard()
                val nextAnimFrag = nextFragment as BaseFragmentWithoutViewModel<*>
                fragmentTransaction.setUpTransactionWithAnimation(nextAnimFrag.transitionAnimation)
                currentFragment?.parentFragmentManager?.executePendingTransactions()
            }
        }
    }

    fun getMainActivityNavigator(): AppNavigator{
        return navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initViews()
    }

    open fun initViews(){

    }

    private var lastNavigator: AppNavigator? = null


    override fun onResume() {
        super.onResume()
        testApp.setNavigatorToHolder(lastNavigator ?: navigator)
    }

    override fun onPause() {
        lastNavigator = testApp.getLastNavigator()
        testApp.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onBackPressed() {
        var backPressedListener: OnBackPressedFragmentsListener? = null
        supportFragmentManager.executePendingTransactions()
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

    companion object{
        fun Class<out BaseActivityWithNavigator>.asScreen(context: Context): ActivityScreen{
            return ActivityScreen{ Intent(context, this) }
        }


    }
}