package com.sayyed.onlineclothingapplication.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.response.ProductResponse
import com.sayyed.onlineclothingapplication.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProductViewModel(private val productRepository: ProductRepository): ViewModel(), Observable {

    lateinit var product: Product

    fun uploadImage(body: MultipartBody.Part) = liveData {
        emit (Resource.loading(data = null))
        try {
            val image = productRepository.uploadImage(body)
            println("error message=>$image")
            emit(Resource.success(data= image))
        } catch (ex: java.lang.Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }

    fun getProductsOfCategory(id: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = productRepository.getProductsOfCategory(id)))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!" ))
        }
    }

    fun addProduct(token: String)  = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val product = productRepository.addProduct(token)
            emit(Resource.success(data = product))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }

    fun updateProduct(
            token: String,
            id: String,
            name: String,
            price: Int,
            description: String,
            image: String,
            brand: String,
            countInStock: Int
    )  = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val product = productRepository.updateProduct(token, id, name, price, description, image, brand, countInStock)
            emit(Resource.success(data = product))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }

    fun getProductsById(id: String)  = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            product = productRepository.getProductById(id).product
            emit(Resource.success(data = product))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }



    fun getAllProducts() = liveData(Dispatchers.IO) {

        emit(Resource.loading(data = null))
        try {
            val data = productRepository.getAllProducts()
            emit(Resource.success(data = data))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }

    fun insertProductToRoom() = viewModelScope.launch {
        try {

            val productLive = productRepository.getAllProducts().product
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
    fun retrieveProductByIdFromRoom(id: String) = productRepository.retrieveProductByIdFromRoom(id)



    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }




}