package com.example.testapp.data.network.response


import com.google.gson.annotations.SerializedName

open class Error(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)