package com.example.testapp.di.modules

import com.example.testapp.BuildConfig
import com.example.testapp.data.network.TestApi
import com.example.testapp.data.network.callAdapter.EitherCallAdapterFactory
import com.example.testapp.data.network.callAdapter.OptionTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
object NetworkModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val factory = OptionTypeAdapter.getFactory()
        val gson = GsonBuilder().registerTypeAdapterFactory(factory).create()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideRetrofit(gsonFactory: GsonConverterFactory): Retrofit {
        val contentType = "application/json".toMediaType()
        val factory = Json.asConverterFactory(contentType)
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonFactory)
            .addConverterFactory(factory)
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideTestApi(retrofit: Retrofit): TestApi {
        return retrofit.create(TestApi::class.java)
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideGson(): Gson {
        return Gson()
    }
}