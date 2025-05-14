package com.navin.ecommerceapp.repository

import com.navin.ecommerceapp.api.ApiServices
import com.navin.ecommerceapp.models.Product
import javax.inject.Inject

/**
 * Repository class responsible for fetching product data.
 *
 * This class acts as a bridge between the API service and the rest of the application.
 * It uses the [ApiServices] to fetch a list of products from the remote server.
 *
 * @property apiServices The API service instance used to interact with the backend.
 */
class ProductRepository @Inject constructor(private val apiServices: ApiServices) {
    /**
     * Fetches the list of products from the API.
     *
     * This suspend function calls the [getAllProducts] method of [ApiServices]
     * to retrieve a list of products asynchronously.
     *
     * @return A list of [Product] objects representing the products.
     * @throws Exception If an error occurs during the API call.
     */
    suspend fun fetchProduct(): List<Product> {
        return apiServices.getAllProducts()
    }
}