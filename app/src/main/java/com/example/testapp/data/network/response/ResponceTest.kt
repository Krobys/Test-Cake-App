package com.example.testapp.data.network.response

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

class ResponseTest(@Nullable
                   @SerializedName("response")
                   val data: Any?,
                   @SerializedName("error")
                   val error: Error?,
                   @SerializedName("success")
                   val success: Boolean?) {

}