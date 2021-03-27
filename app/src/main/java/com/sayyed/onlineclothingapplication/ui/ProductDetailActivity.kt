package com.sayyed.onlineclothingapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityProductDetailBinding
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.utils.Network
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModel
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.coroutineContext

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var productViewModel: ProductViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        binding = DataBindingUtil.setContentView(this@ProductDetailActivity, R.layout.activity_product_detail)

        /*-----------------------GET CATEGORY DATA FROM DASHBOARD ACTIVITY THROUGH INTENT-------------------------*/
        val productId = intent.getStringExtra("product_id")


        /*----------------------PRODUCT ACTIVITY AND PRODUCT VIEW MODEL CONNECTION--------------------------------*/
        setupViewModel()

        /*------------------------FUNCTION CALLED AND DISPLAYED CATEGORIZED DATA----------------------------------*/
        setProductObserver(productId.toString())

    }


    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val productDao: ProductDAO = OnlineClothingDB.getInstance(application).productDAO
        val repository = ProductRepository(productDao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)

    }

    /*-----------------------------------------GET PRODUCTS OF ID-------------------------------------------------*/
    private fun setProductObserver(id: String){
        productViewModel.getProductsById(id).observe(this, {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { product ->
                        setupUI(product)
                        }
                    }
                }
            }
        })
    }

    /*---------------------------------------------SET UP UI------------------------------------------------------*/
    private fun setupUI(product: Product) {
        val stock = product.countInStock
        val price = product.price.toDouble()
        binding.tvProductTitle.text = product.name
        binding.tvProductPrice.text = price.toString()
        binding.tvProductBrand.text = product.brand
        binding.tvProductDescription.text = product.description
        binding.npProductQty.maxValue = product.countInStock
        binding.ratingBarProduct.rating = product.rating.toFloat()
        binding.imgProduct.contentDescription = product.name
        Glide.with(this@ProductDetailActivity)
            .load(product.image)
            .into(binding.imgProduct)

        println(product.reviews)
        toggleProductDescription()
        toggleCheckoutBtn(stock)
    }

    /*------------------------------------DESCRIPTION TOGGLE------------------------------------------------------*/
    private fun toggleProductDescription() {
        binding.tvProductDescriptionToggle.setOnClickListener {
            if(binding.tvProductDescription.visibility == View.GONE) {
                binding.tvProductDescription.visibility = View.VISIBLE
            } else {
                binding.tvProductDescription.visibility = View.GONE
            }
        }
    }

    /*----------------------------CHECKOUT BUTTON TOGGLE----------------------------------------------------------*/
    private fun toggleCheckoutBtn(stock: Int) {
        if (stock > 0) {
            binding.btnAddToCart.setOnClickListener {
                binding.btnCheckout.visibility = View.VISIBLE
            }
        }

    }



}