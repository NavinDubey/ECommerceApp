package com.navin.ecommerceapp.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * NetworkModule provides the necessary network dependencies using Dagger Hilt.
 * This module is installed in the SingletonComponent to make these dependencies available throughout the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides the base URL for the API.
     * @return The base URL for the network requests.
     */
    @Provides
    fun getBaseUrl() = "https://fakestoreapi.com"


    /**
     * Provides a singleton instance of [Retrofit] configured with the base URL and Gson converter.
     * @return The [Retrofit] instance used to make network requests.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    /**
     * Provides the singleton instance of [ApiServices], which contains the network API methods.
     * @param retrofit The Retrofit instance to create the service.
     * @return The [ApiServices] instance.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }
}
