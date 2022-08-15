package com.example.testapp.base

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable

abstract class BaseDialog<DataBindingClass : ViewDataBinding>(context: Context) : AlertDialog(context) {

    protected abstract val layoutId: Int
    protected open val dialogAnimationResourceTheme: Int? = null

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected open val gravity: Int = Gravity.CENTER
    protected open val isTransparentShadow: Boolean = false
    protected open val isCancelledOnTouchOutside: Boolean = true
    lateinit var binding: DataBindingClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
        setContentView(binding.root)
        initViews()
    }

    open fun initViews(){

    }

    override fun show() {
        preShow()
        super.show()
        afterShow()
    }

    private fun preShow(){
        setCanceledOnTouchOutside(isCancelledOnTouchOutside)
        askHideNavigation()
        window?.apply {
            dialogAnimationResourceTheme?.let {
                window?.attributes?.windowAnimations = it
            }
        }
    }

    private fun afterShow(){
        window?.apply {
            attributes.gravity = gravity
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            if (isTransparentShadow){
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun askHideNavigation() {
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }
}