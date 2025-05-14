package com.navin.ecommerceapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navin.ecommerceapp.models.Product
import com.navin.ecommerceapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for managing product data and interacting with the ProductRepository.
 * It provides LiveData for the product list and error messages.
 */
@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {
    private val _product = MutableLiveData<List<Product>>()
    val product: LiveData<List<Product>> get() = _product

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun getProductList() {
        viewModelScope.launch {
            try {
                val response = repository.fetchProduct()
                _product.postValue(response)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}