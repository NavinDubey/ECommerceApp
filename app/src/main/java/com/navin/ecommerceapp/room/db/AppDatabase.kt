package com.navin.ecommerceapp.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.navin.ecommerceapp.models.Cart
import com.navin.ecommerceapp.models.Product
import com.navin.ecommerceapp.room.Dao.CartDao
import com.navin.ecommerceapp.room.Dao.ProductDao

@Database(entities = [Product::class, Cart::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "ecommerce_db"

        /**
         * Returns a singleton instance of [AppDatabase].
         * Ensures only one database instance exists across the app.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration() // Consider custom migration in production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
