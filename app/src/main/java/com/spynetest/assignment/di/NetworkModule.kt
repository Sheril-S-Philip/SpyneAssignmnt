package com.spynetest.assignment.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.spynetest.assignment.api.ApiService
import com.spynetest.assignment.util.Constants.Companion.BASE_URL
import com.spynetest.assignment.util.Constants.Companion.NETWORK_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideBaseUrl() : String = BASE_URL

    @Provides
    @Singleton
    fun connectionTimeout() = NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun provideGson() : Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideLogging() : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOKHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor).build()


    @Provides
    @Singleton
    fun providesRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient) : ApiService{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}