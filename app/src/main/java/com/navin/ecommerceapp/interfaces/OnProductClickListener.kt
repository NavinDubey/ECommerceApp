package com.navin.ecommerceapp.interfaces

import com.navin.ecommerceapp.models.Product


/**
 * Interface to handle product click events in the product list.
 * This interface defines methods to perform actions when a product is interacted with.
 */
interface OnProductClickListener {
    /**
     * Called when the "Add to Cart" button is clicked for a product.
     * @param product The [Product] object that was clicked.
     */
    fun onAddToCartClick(product: Product)
}
