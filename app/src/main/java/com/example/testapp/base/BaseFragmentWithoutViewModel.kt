package com.example.testapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.testapp.R
import com.example.testapp.TestApp
import com.example.testapp.custom.dialogs.DefaultErrorDialog
import com.example.testapp.data.network.response.Error
import com.example.testapp.tools.getSystemInsets
import com.example.testapp.tools.hideKeyboard
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.jaeger.library.StatusBarUtil
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragmentWithoutViewModel<DataBinding : ViewDataBinding> : DaggerFragment(), OnBackPressedFragmentsListener {
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected val compositeSubscription: CompositeSubscription = CompositeSubscription()

    @Inject
    protected lateinit var testApp: TestApp

    protected lateinit var binding: DataBinding
    protected abstract val layoutId: Int
    open val router get() = (activity as BaseActivityWithNavigator).router

    open var isLightStatusBar: Boolean = false

    var transitionAnimation: TransitionAnimation = TransitionAnimation.NO_ANIMATION

    protected var progressBar: ProgressBar? = null

    fun asScreen(): FragmentScreen{
        return FragmentScreen.invoke(this.javaClass.canonicalName){
            this
        }
    }

    fun asScreen(data: Bundle): FragmentScreen{
        return FragmentScreen.invoke(this.javaClass.canonicalName){
            this.apply {
                arguments = data
            }
        }
    }

    fun asScreenAnimated(animation: TransitionAnimation): FragmentScreen{
        return FragmentScreen.invoke(this.javaClass.canonicalName){
            this.apply {
               transitionAnimation = animation
            }
        }
    }

    var container: ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        //binding.executePendingBindings()
        this.container = container
        return binding.root
    }

    private fun setUpStatusBar(){
        if (isLightStatusBar){
            StatusBarUtil.setDarkMode(activity)
        }else{
            StatusBarUtil.setLightMode(activity)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestSystemInsets()
        setUpStatusBar()
        initViews()
    }

    fun requestSystemInsets(){
        activity?.window?.decorView?.getSystemInsets { statusBarSize, navigationBarSize, isKeyboardOpen ->
            applyInsetsPadding(statusBarSize, navigationBarSize, isKeyboardOpen)
        }
    }

    abstract fun initViews()

    override fun onDestroyView() {
        compositeDisposable.dispose()
        compositeSubscription.clear()
        activity?.hideKeyboard()
        super.onDestroyView()
    }

    open fun onBackPressedFragment(): Boolean { // переопределить для изменения поведения возврата назад, не вызывать в коде для возврата назад, это обработчик
        return false
    }

    override fun onBackPressed(): Boolean { // не вызывать в коде для возврата назад, это обработчик
        return onBackPressedFragment()
    }

    inline fun <reified T> MutableLiveData<T>.observe(crossinline observe: ((value: T) -> Unit)) {
        this.observe(viewLifecycleOwner, Observer {
            observe.invoke(it)
        })
    }

    private var errorDialog: DefaultErrorDialog? = null

    fun showDefaultErrorDialog(error: Error){
        var errorMessage = error.message
        when(error.code){
            0 -> { // Нет соединения с хостом
                errorMessage = getString(R.string.error_connection_text)
            }
            else -> {

            }
        }
        if (errorDialog?.isShowing != true) {
            errorDialog = DefaultErrorDialog(requireContext(), errorMessage)
            errorDialog?.show()
        }

        Timber.d("HANDLE ERROR: ${error.code}, ${error.message}")

    }

    open fun applyInsetsPadding(systemStatusBarSize: Int, systemNavigationBarSize: Int, isKeyboardOpen: Boolean){

    }

    fun TODO(){
        Toast.makeText(context, "В разработке", Toast.LENGTH_SHORT).show()
    }

    enum class TransitionAnimation{
        HORISONTAL, HORISONTAL_SIMPLE, VERTICAL, VERTICAL_SIMPLE, FADE, NO_ANIMATION
    }
}