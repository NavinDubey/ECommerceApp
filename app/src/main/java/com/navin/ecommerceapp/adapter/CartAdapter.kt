package com.navin.ecommerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.navin.ecommerceapp.databinding.ItemCartBinding
import com.navin.ecommerceapp.models.Cart


/**
 * Adapter to show cart items using ListAdapter for efficient updates.
 */
class CartAdapter(
    private var cartList: List<Cart>,
    private val listener: CartItemListener
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    /**
     * Interface to handle cart item interactions.
     */
    interface CartItemListener {
        fun onQuantityChange(cart: Cart, isIncrease: Boolean)
        fun onRemove(cart: Cart)
    }

    fun updateList(newList: List<Cart>) {
        cartList = newList
        notifyDataSetChanged()
    }

    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = cartList[position]
        holder.binding.apply {
            txtTitle.text = cart.title
            txtPrice.text = "₹${cart.price}"
            txtQuantity.text = cart.quantity.toString()

            btnIncrease.setOnClickListener { listener.onQuantityChange(cart, true) }
            btnDecrease.setOnClickListener { listener.onQuantityChange(cart, false) }
            btnRemove.setOnClickListener { listener.onRemove(cart) }
        }
    }

    override fun getItemCount() = cartList.size
}
