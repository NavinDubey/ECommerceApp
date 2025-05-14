package com.navin.ecommerceapp.api

import com.navin.ecommerceapp.models.Product
import retrofit2.http.GET


/**
 * API Service interface to define network requests.
 */
interface ApiServices {
    @GET("products")
    suspend fun getAllProducts(): List<Product>
}