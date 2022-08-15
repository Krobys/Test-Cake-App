package com.example.testapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.testapp.data.network.response.CakeResponse

@Dao
interface CakesDao {

    @Insert
    suspend fun setCakes(users: List<CakeResponse.CakeResponseItem>)

    @Query("SELECT * FROM CakeResponseItem")
    suspend fun getCakes(): List<CakeResponse.CakeResponseItem>

    @Query("DELETE FROM CakeResponseItem")
    suspend fun clearTable()
}