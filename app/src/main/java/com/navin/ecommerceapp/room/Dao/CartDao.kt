package com.navin.ecommerceapp.room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.navin.ecommerceapp.models.Cart


/**
 * Data Access Object (DAO) interface for the [Cart] entity.
 *
 * This interface contains methods to interact with the [Cart] table in the Room database.
 * It provides functions to add, retrieve, update, and delete cart items in the database.
 */
@Dao
interface CartDao {

    @Insert
    suspend fun addToCart(cart: Cart)

    @Query("SELECT * FROM cart")
    suspend fun getAllCartItems(): List<Cart>

    @Query("SELECT * FROM cart WHERE productId = :productId")
    suspend fun getCartItemByProductId(productId: Int): Cart?

    @Update
    suspend fun updateCart(cart: Cart)

    @Delete
    suspend fun removeCartItem(cart: Cart)

}
