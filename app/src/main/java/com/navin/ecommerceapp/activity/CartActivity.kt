package com.navin.ecommerceapp.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.navin.ecommerceapp.adapter.CartAdapter
import com.navin.ecommerceapp.databinding.ActivityCartBinding
import com.navin.ecommerceapp.models.Cart
import com.navin.ecommerceapp.room.db.AppDatabase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * CartActivity displays the list of cart items stored in the local Room database.
 * Allows users to increase/decrease quantity or remove items from the cart.
 * Shows total price and item count.
 */
class CartActivity : AppCompatActivity(), CartAdapter.CartItemListener {

    private lateinit var binding: ActivityCartBinding
    private lateinit var db: AppDatabase
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        initDatabase()
        setupRecyclerView()
        loadCartItems()
    }

    /**
     * Initializes the Room database instance.
     */
    private fun initDatabase() {
        db = AppDatabase.getInstance(applicationContext)
    }

    /**
     * Sets up the RecyclerView and CartAdapter.
     */
    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(emptyList(), this)
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }
    }

    /**
     * Loads cart items from the database, handles errors and empty state.
     */
    private fun loadCartItems() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Toast.makeText(
                this,
                "Error loading cart: ${exception.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }

        lifecycleScope.launch(handler) {
            val cartItems = db.cartDao().getAllCartItems()

            if (cartItems.isEmpty()) {
                showEmptyState()
            } else {
                showCartState(cartItems)
            }
        }
    }

    /**
     * Updates the UI with cart items and total price/item count.
     */
    private fun showCartState(cartItems: List<Cart>) {
        cartAdapter.updateList(cartItems)
        binding.cartRecyclerView.visibility = View.VISIBLE
        binding.emptyCartView.visibility = View.GONE

        val totalItems = cartItems.sumOf { it.quantity }
        val totalPrice = cartItems.sumOf { it.price * it.quantity }

        binding.txtTotalItems.text = "Total Items: $totalItems"
        binding.txtTotalPrice.text = "Total Price: ₹%.2f".format(totalPrice)
    }

    /**
     * Displays empty state view when there are no items in the cart.
     */
    private fun showEmptyState() {
        cartAdapter.updateList(emptyList())
        binding.cartRecyclerView.visibility = View.GONE
        binding.emptyCartView.visibility = View.VISIBLE
        binding.txtTotalItems.text = "Total Items: 0"
        binding.txtTotalPrice.text = "Total Price: ₹0.00"
    }

    /**
     * Called when quantity changes. Increases or decreases the item quantity.
     */
    override fun onQuantityChange(cart: Cart, isIncrease: Boolean) {
        lifecycleScope.launch {
            val newQuantity = if (isIncrease) cart.quantity + 1 else cart.quantity - 1
            if (newQuantity > 0) {
                db.cartDao().updateCart(cart.copy(quantity = newQuantity))
            } else {
                db.cartDao().removeCartItem(cart)
                Toast.makeText(this@CartActivity, "Item removed", Toast.LENGTH_SHORT).show()
            }
            loadCartItems()
        }
    }

    /**
     * Called when the user removes an item from the cart.
     */
    override fun onRemove(cart: Cart) {
        lifecycleScope.launch {
            db.cartDao().removeCartItem(cart)
            loadCartItems()
            Toast.makeText(this@CartActivity, "Item removed", Toast.LENGTH_SHORT).show()
        }
    }
}
