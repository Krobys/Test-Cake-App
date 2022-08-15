package com.example.testapp.di.modules

import androidx.room.Room
import com.example.testapp.TestApp
import com.example.testapp.data.database.TestDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object DatabaseModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideTestDatabase(app: TestApp): TestDatabase {
        return Room
            .databaseBuilder(app, TestDatabase::class.java, "test.database")
            .fallbackToDestructiveMigration()
            .build()
    }

}