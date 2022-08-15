package com.example.testapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testapp.data.database.dao.CakesDao
import com.example.testapp.data.network.response.CakeResponse

@Database(
    entities = [
        CakeResponse.CakeResponseItem::class
    ], version = 1, exportSchema = false
)
abstract class TestDatabase: RoomDatabase()  {

    abstract fun cakeDao(): CakesDao

}