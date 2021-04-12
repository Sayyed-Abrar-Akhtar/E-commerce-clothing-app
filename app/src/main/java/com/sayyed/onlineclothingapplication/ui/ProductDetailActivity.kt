package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.adapter.ReviewAdapter
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityProductDetailBinding
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.utils.FileUpload
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
    private lateinit var reviewAdapter: ReviewAdapter

    private var maxStock: Int = 0
    private var qtySelected: Int = 1

    private var idSharedPref : String? = ""
    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var usernameSharedPref : String? = ""
    private  var emailSharedPref : String? = ""
    private  var passwordSharedPref : String? = ""
    private  var tokenSharedPref : String? = ""
    private  var isAdminSharedPref : Boolean = false
    private  var contactSharedPref : String? = ""
    private var isSuccess: Boolean =  false
    private var isLoading: Boolean =  false

    private var product_image: String = ""
    private var product_title: String = ""
    private var product_price: Float = 0f





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        binding = DataBindingUtil.setContentView(this@ProductDetailActivity, R.layout.activity_product_detail)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()

        /*-----------------------GET CATEGORY DATA FROM DASHBOARD ACTIVITY THROUGH INTENT-------------------------*/
        val productId = intent.getStringExtra("product_id")


        /*----------------------PRODUCT ACTIVITY AND PRODUCT VIEW MODEL CONNECTION--------------------------------*/
        setupViewModel()

        /*------------------------FUNCTION CALLED AND DISPLAYED CATEGORIZED DATA----------------------------------*/
        setProductObserver(productId.toString())


        when (Network.isNetworkAvailable(this)) {
            true -> {
                setProductObserver(productId.toString())
            }
            false -> {
                loadCategorisedProductFromRoom(productId.toString())
            }
        }

        /*--------------------------------INCREASE QTY------------------------------------------------------------*/
        binding.imgIncreaseQty.setOnClickListener{
            if(qtySelected <= maxStock && maxStock > 0) {
                qtySelected += 1
                binding.tvQty.text = qtySelected.toString()
            }
        }

        /*--------------------------------DECREASE QTY------------------------------------------------------------*/
        binding.imgDecreaseQty.setOnClickListener{
            if(qtySelected > 1 && maxStock > 0) {
                qtySelected -= 1
                binding.tvQty.text = qtySelected.toString()
            }
        }

        /*--------------------------------CHECKOUT CLICK----------------------------------------------------------*/

        binding.btnCheckout.setOnClickListener {
            val intent = Intent(this@ProductDetailActivity, CartActivity::class.java)
            intent.putExtra("product_title", "$product_title")
            intent.putExtra("product_image", "$product_image")
            intent.putExtra("product_price", "$product_price")
            intent.putExtra("product_qty", "${binding.tvQty.text}")
            startActivity(intent)
        }
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
                    Status.ERROR -> {
                        println("=========================ERROR====================")
                        println(resource.data)
                        println(resource.message)
                        println("==================================================")
                    }
                    Status.LOADING -> {
                        println("=========================LOADER====================")
                        println("!!! LOADING... !!!")
                        println("===================================================")
                    }
                }
            }
        })
    }


    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        idSharedPref = sharedPref.getString("_id", "")
        emailSharedPref = sharedPref.getString("email", "")
        usernameSharedPref = sharedPref.getString("username", "")
        passwordSharedPref = sharedPref.getString("password", "")
        firstNameSharedPref = sharedPref.getString("firstName", "")
        lastNameSharedPref = sharedPref.getString("lastName", "")
        imageSharedPref = sharedPref.getString("image", "")
        contactSharedPref = sharedPref.getString("contact", "")
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
        tokenSharedPref = sharedPref.getString("token", "")

    }




    /*-----------------------GET PRODUCTS OF ID FROM ROOM TO DISPLAY--------------------------------------------*/
    private fun loadCategorisedProductFromRoom(productId: String) {
        productViewModel.retrieveProductByIdFromRoom(productId).observe(this, {
            it?.let { product ->
                setupUI(product)
            }
        })
    }

    /*---------------------------------------------SET UP UI------------------------------------------------------*/
    private fun setupUI(product: Product) {
        val stock = product.countInStock
        val price = product.price.toDouble()
        maxStock = product.countInStock

        // SETTING UP TEXT ON PRODUCT DETAIL VIEWS
        binding.tvProductTitle.text = product.name
        binding.tvProductPrice.text = price.toString()
        binding.tvProductBrand.text = product.brand
        binding.tvProductDescription.text = product.description
        binding.tvQty.text = qtySelected.toString()
        binding.ratingBarProduct.rating = product.rating.toFloat()
        binding.imgProduct.contentDescription = product.name

        product_title = product.name
        product_price = product.price.toFloat()
        product_image = FileUpload.checkImageString(product.image)


        // LOADING PRODUCT IMAGE FROM API USING GLIDE
        Glide.with(this@ProductDetailActivity)
            .load(product_image)
            .into(binding.imgProduct)

        // DISPLAYED CUSTOMER REVIEWS LIST USING RECYCLER VIEW
        binding.recyclerViewReviews.layoutManager = LinearLayoutManager(this@ProductDetailActivity)
        reviewAdapter = ReviewAdapter(this@ProductDetailActivity, product.reviews)
        binding.recyclerViewReviews.adapter = reviewAdapter
        reviewAdapter.notifyDataSetChanged()

        // TOGGLE FUNCTION TO SHOW HIDE VIEWS
        toggleProductDescription()
        toggleCheckoutBtn(stock)
        toggleReviews()
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

    /*------------------------------------REVIEWS TOGGLE---------------------------------------------------------*/
    private fun toggleReviews() {
        binding.tvProductReviewToggle.setOnClickListener {
            if(binding.recyclerViewReviews.visibility == View.GONE) {
                binding.recyclerViewReviews.visibility = View.VISIBLE
            } else {
                binding.recyclerViewReviews.visibility = View.GONE
            }
        }
    }

}