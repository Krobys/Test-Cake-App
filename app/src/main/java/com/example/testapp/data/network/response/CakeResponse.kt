package com.example.testapp.data.network.response


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class CakeResponse : ArrayList<CakeResponse.CakeResponseItem>(){

    @Entity
    data class CakeResponseItem(
        @SerializedName("desc")
        var desc: String,
        @SerializedName("image")
        var image: String,
        @SerializedName("title")
        var title: String
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
    }
}