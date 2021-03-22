package com.sayyed.onlineclothingapplication.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository): ViewModel(), Observable {

    fun getProductsOfCategory(category: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = productRepository.getProductsOfCategory(category)))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!" ))
        }
    }

    fun getAllProducts() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = productRepository.getAllProducts()))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }

    fun insertProductToRoom() = viewModelScope.launch {
        try {
            val productLive = productRepository.getAllProducts().Product
            for(product in productLive) {
                productRepository.insertProductIntoRoom(product)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    fun deleteProductsFromRoom() = viewModelScope.launch {
        try {
            productRepository.deleteProductsFromRoom()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    val retrieveProductsFromRoom = productRepository.retrieveProductsFromRoom
    fun retrieveCategorizedProductsFromRoom(category: String) = productRepository.retrieveCategorizedProductsFromRoom(category)



    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }




}