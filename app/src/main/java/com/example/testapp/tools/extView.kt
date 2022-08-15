package com.example.testapp.tools

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentTransaction
import com.example.testapp.R
import com.example.testapp.base.BaseFragmentWithoutViewModel

fun View.setVisible(isVisible: Boolean){
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun ViewGroup.setPaddingTop(padding: Int){
    this.setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}

fun ViewGroup.setPaddingBottom(padding: Int){
    this.setPadding(paddingLeft, paddingTop, paddingRight, padding)
}

fun Activity.hideKeyboard() {
    this.currentFocus?.run {
        val imm = this@hideKeyboard.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun FragmentTransaction.setUpTransactionWithAnimation(transitionAnimation: BaseFragmentWithoutViewModel.TransitionAnimation){
    when(transitionAnimation){
        BaseFragmentWithoutViewModel.TransitionAnimation.HORISONTAL -> {
            setCustomAnimations(
                    R.anim.slide_right_in,
                    R.anim.slide_left_out,
                    R.anim.slide_left_in,
                    R.anim.slide_right_out)
        }
        BaseFragmentWithoutViewModel.TransitionAnimation.HORISONTAL_SIMPLE -> {
            setCustomAnimations(
                    R.anim.anim_nothing,
                    R.anim.anim_nothing,
                    R.anim.anim_nothing,
                    R.anim.slide_right_out)
        }
        BaseFragmentWithoutViewModel.TransitionAnimation.VERTICAL -> {
            setCustomAnimations(
                    R.anim.slide_top_in,
                    R.anim.slide_bottom_out,
                    R.anim.slide_bottom_in,
                    R.anim.slide_top_out)
        }
        BaseFragmentWithoutViewModel.TransitionAnimation.FADE -> {
            setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out)
        }
    }
}

fun View.getSystemInsets(listener: OnSystemBarsSizeChangedListener) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        val hasKeyboard = isKeyboardAppeared(insets.systemWindowInsetBottom)
        listener(insets.systemWindowInsetTop, insets.systemWindowInsetBottom, hasKeyboard)
        insets
    }
}

typealias OnSystemBarsSizeChangedListener =
        (statusBarSize: Int, navigationBarSize: Int, isKeyboardOpen: Boolean) -> Unit


private fun View.isKeyboardAppeared(bottomInset: Int) =
        bottomInset / this.resources.displayMetrics.heightPixels.toDouble() > .25