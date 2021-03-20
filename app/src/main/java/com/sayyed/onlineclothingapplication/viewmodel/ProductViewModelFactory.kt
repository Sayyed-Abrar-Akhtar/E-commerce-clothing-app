package com.sayyed.onlineclothingapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import java.lang.IllegalArgumentException

class ProductViewModelFactory(private val productRepository: ProductRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }



}
































