package com.navin.ecommerceapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.navin.ecommerceapp.activity.CartActivity
import com.navin.ecommerceapp.adapter.ProductAdapter
import com.navin.ecommerceapp.databinding.ActivityMainBinding
import com.navin.ecommerceapp.interfaces.OnProductClickListener
import com.navin.ecommerceapp.models.Cart
import com.navin.ecommerceapp.models.Product
import com.navin.ecommerceapp.room.db.AppDatabase
import com.navin.ecommerceapp.viewModel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * MainActivity displays the product catalog fetched from ViewModel (API or local source),
 * and allows users to add items to the cart.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnProductClickListener {

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initially hide the cart layout when activity starts
        binding.layoutCart.visibility = View.GONE

        initDatabase()
        setupRecyclerView()
        observeProductList()
        setupClickListeners()
        checkCartItems()
    }

    /**
     * Checks the cart item count and updates the cart layout visibility.
     */
    private fun checkCartItems() {
        lifecycleScope.launch {
            val cartItems = db.cartDao().getAllCartItems()
            binding.layoutCart.visibility = if (cartItems.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    /**
     * Called when the activity resumes, e.g. after returning from CartActivity.
     */
    override fun onResume() {
        super.onResume()
        checkCartItems()
    }

    /**
     * Initializes the Room database instance.
     */
    private fun initDatabase() {
        db = AppDatabase.getInstance(applicationContext)
    }

    /**
     * Configures the RecyclerView with a ProductAdapter.
     */
    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList(), this)
        binding.recyclerview.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    /**
     * Observes product list LiveData and handles empty/loading/error states.
     */
    private fun observeProductList() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getProductList()

        viewModel.product.observe(this) { products ->
            binding.progressBar.visibility = View.GONE
            if (products.isNullOrEmpty()) {
                Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show()
            } else {
                productAdapter.updateProduct(products)
            }
        }

        viewModel.error.observe(this) { error ->
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Sets up click listeners for UI interactions.
     */
    private fun setupClickListeners() {
        binding.layoutCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    /**
     * Handles "Add to Cart" button click for a product.
     * Inserts or updates the cart item in the local Room database.
     */
    override fun onAddToCartClick(product: Product) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Toast.makeText(this, "Cart error: ${exception.localizedMessage}", Toast.LENGTH_LONG)
                .show()
        }

        lifecycleScope.launch(handler) {
            val existingProduct = db.productDao().getProductByTitle(product.title)
            val productId = existingProduct?.id ?: db.productDao().insertProduct(product).toInt()

            val existingCartItem = db.cartDao().getCartItemByProductId(productId)
            if (existingCartItem != null) {
                val updatedCartItem =
                    existingCartItem.copy(quantity = existingCartItem.quantity + 1)
                db.cartDao().updateCart(updatedCartItem)
            } else {
                val newCartItem = Cart(
                    productId = productId,
                    title = product.title,
                    price = product.price,
                    quantity = 1
                )
                db.cartDao().addToCart(newCartItem)
            }

            // Update the cart visibility
            checkCartItems()

            // Show a success message
            Toast.makeText(this@MainActivity, "Added to cart", Toast.LENGTH_SHORT).show()
        }
    }
}
