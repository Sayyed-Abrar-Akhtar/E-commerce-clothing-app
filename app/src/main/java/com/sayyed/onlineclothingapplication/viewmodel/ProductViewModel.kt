package com.sayyed.onlineclothingapplication.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayyed.onlineclothingapplication.entities.Product
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.ui.DashboardActivity
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository): ViewModel(), Observable {

    val products = productRepository.products

    val productImage = MutableLiveData<String>()
    val productTitle = MutableLiveData<String>()
    val productPrice = MutableLiveData<String>()

    init {
        productTitle.value = "Title"
        productPrice.value = "NPR 1000"
    }

    fun addProduct() {
        allProducts()
    }

    fun deleteProduct() {

    }


    fun insertProduct(product: Product) = viewModelScope.launch {
            productRepository.insert(product)
    }

    fun updateProduct(product: Product) = viewModelScope.launch {
            productRepository.update(product)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
            productRepository.delete(product)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    private fun allProducts() {
        insertProduct(
        Product(
                productId = 0,
                "Blue T-shirt",
                "NPR 1500",
                "100% cotton",
                "Blue",
                "Small",
                "https://cdn.pixabay.com/photo/2013/07/13/14/07/apparel-162180_960_720.png"
        )
        )
        insertProduct(
        Product(
                productId = 0,
                "Blue Pant",
                "NPR 3500",
                "100% cotton",
                "Blue",
                "Large",
                "https://cdn.pixabay.com/photo/2014/04/02/10/40/jeans-304196_960_720.png"
        )
        )
        insertProduct(

        Product(
                productId = 0,
                "Baby Shoe",
                "NPR 6000",
                "100% Leather",
                "Green",
                "Small",
                "https://cdn.pixabay.com/photo/2014/10/27/19/18/baby-shoes-505471_960_720.jpg"
        )
        )
        insertProduct(
        Product(
                productId = 0,
                "Woollen Shoe",
                "NPR 6900",
                "100% Wool",
                "Pink",
                "Small",
                "https://cdn.pixabay.com/photo/2014/10/27/19/18/baby-shoes-505471_960_720.jpg"
        )
        )
        insertProduct(
        Product(
                productId = 0,
                "Baby Scarf",
                "NPR 799",
                "100% Wool",
                "Grey",
                "Small",
                "https://cdn.pixabay.com/photo/2015/12/21/05/45/girl-1102086_960_720.jpg"
        )


        )
        insertProduct(
        Product(
                productId = 0,
                "Sweater",
                "NPR 3500",
                "100% wool",
                "Navy",
                "Small",
                "https://cdn.pixabay.com/photo/2016/03/27/19/31/fashion-1283863_960_720.jpg"
        )


        )
        insertProduct(
        Product(
                productId = 0,
                "Baby Hat",
                "NPR 1500",
                "100% Polyester",
                "Stripe",
                "Small",
                "https://cdn.pixabay.com/photo/2016/05/17/22/16/baby-1399332_960_720.jpg"
        )
        )
    }


}