package com.sayyed.onlineclothingapplication.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayyed.onlineclothingapplication.entities.Product
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository): ViewModel() {

    val products = productRepository.products

    val productImage = MutableLiveData<String>()
    val productTitle = MutableLiveData<String>()
    val productPrice = MutableLiveData<String>()

    init {
        productTitle.value = "Title"
        productPrice.value = "NPR 1000"
    }

    fun insertProduct(product: Product) =
        viewModelScope.launch {
            productRepository.insert(product)
        }

    fun updateProduct(product: Product) =
        viewModelScope.launch {
            productRepository.update(product)
        }

    fun deleteProduct(product: Product) =
        viewModelScope.launch {
            productRepository.delete(product)
        }

}