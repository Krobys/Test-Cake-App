package com.example.testapp.tools

import com.example.testapp.base.BaseFragmentWithoutViewModel
import com.github.terrakok.cicerone.androidx.FragmentScreen

fun Class<out BaseFragmentWithoutViewModel<*>>.asScreenAnimated(
    animation: BaseFragmentWithoutViewModel.TransitionAnimation,
    isCreateNew: Boolean = true
): FragmentScreen {
    return this.createNewFragmentScreen(animation)
}

private fun Class<out BaseFragmentWithoutViewModel<*>>.createNewFragmentScreen(
    animation: BaseFragmentWithoutViewModel.TransitionAnimation? = null
): FragmentScreen{
    val fragment = this.getConstructor().newInstance()
    animation?.let {
        fragment.transitionAnimation = it
    }
    val fragmentScreen = FragmentScreen.invoke(canonicalName) {
        fragment
    }
    return fragmentScreen
}

fun String.validateName(): String{
    return this.lowercase().trim().replaceFirstChar(Char::uppercase)
}