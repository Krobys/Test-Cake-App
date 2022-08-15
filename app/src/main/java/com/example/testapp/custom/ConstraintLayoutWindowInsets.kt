package com.example.testapp.custom

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout

class ConstraintLayoutWindowInsets @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    override fun onApplyWindowInsets(windowInsets: WindowInsets): WindowInsets {
        childCount.let {
            // propagates window insets to children's
            for (index in 0 until it) {
                getChildAt(index).dispatchApplyWindowInsets(windowInsets)
            }
        }
        return windowInsets
    }

}