package com.navin.ecommerceapp.room.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.navin.ecommerceapp.models.Product

/**
 * Data Access Object (DAO) interface for the [Product] entity.
 *
 * This interface provides methods for interacting with the [Product] table
 * in the Room database, such as inserting new products and retrieving products
 * by their ID or title.
 */
@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(product: Product) : Long

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): Product?

    @Query("SELECT * FROM products WHERE title = :title LIMIT 1")
    suspend fun getProductByTitle(title: String): Product?
}
