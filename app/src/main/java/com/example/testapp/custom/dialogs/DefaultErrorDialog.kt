package com.example.testapp.custom.dialogs

import android.content.Context
import com.example.testapp.R
import com.example.testapp.base.BaseDialog
import com.example.testapp.databinding.DialogDefaultErrorBinding

class DefaultErrorDialog(context: Context,
                         private val errorText: String)
    : BaseDialog<DialogDefaultErrorBinding>(context) {
    override val layoutId: Int = R.layout.dialog_default_error

    override fun initViews() {
        binding.textError.text = errorText
    }


}