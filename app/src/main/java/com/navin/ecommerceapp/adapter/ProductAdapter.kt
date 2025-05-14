package com.navin.ecommerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.navin.ecommerceapp.R
import com.navin.ecommerceapp.databinding.ItemProductListBinding
import com.navin.ecommerceapp.interfaces.OnProductClickListener
import com.navin.ecommerceapp.models.Product


/**
 * Adapter for displaying product list with Glide image loading and click handling.
 */
class ProductAdapter(
    private var productList: List<Product>,
    private val listener: OnProductClickListener
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ViewHolder {
        val binding =
            ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateProduct(product: List<Product>) {
        productList = product
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productName.text = product.title
            binding.productPrice.text = "₹"+product.price.toString()

            // Check if the product image URL is null or empty
            if (product.image.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(R.drawable.img_image)
                    .into(binding.imgCard)
            } else {
                Glide.with(binding.root.context)
                    .load(product.image)
                    .apply(RequestOptions().placeholder(R.drawable.img_image))
                    .into(binding.imgCard)
            }

            binding.addToCard.setOnClickListener {
                listener.onAddToCartClick(product)
            }
        }
    }
}
