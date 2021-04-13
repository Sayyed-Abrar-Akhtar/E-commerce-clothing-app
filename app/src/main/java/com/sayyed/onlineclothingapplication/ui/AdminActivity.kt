package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityAdminBinding
import com.sayyed.onlineclothingapplication.databinding.ActivityProductBinding
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.repository.UserRepository
import com.sayyed.onlineclothingapplication.response.CategoryResponse
import com.sayyed.onlineclothingapplication.response.ProductDetailResponse
import com.sayyed.onlineclothingapplication.response.ProductResponse
import com.sayyed.onlineclothingapplication.response.UserResponse
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.*
import java.util.*

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var userViewModel: UserViewModel


    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var contactSharedPref : String? = ""
    private  var tokenSharedPref : String? = ""
    private  var isAdminSharedPref : Boolean = false

    private var isSuccess: Boolean = false
    private var idIntent: String = ""
    private var imageIntent: String = ""
    private var nameIntent: String = ""
    private var priceIntent: Float = 0f
    private var brandIntent: String = ""
    private var descriptionIntent: String = ""
    private var countInStockIntent: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)


        binding = DataBindingUtil.setContentView(this@AdminActivity, R.layout.activity_admin)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()


        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
                this,
                binding.drawer,
                binding.toolbar,
                R.string.open,
                R.string.close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*----------------------------------------NAVIGATION DRAWER LAYOUT----------------------------------------*/
        navigationDrawerSetup = NavigationDrawerSetup()

        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "Admin Dashboard")

        navigationDrawerSetup.addHeaderText(
                this@AdminActivity,
                binding.navigationView,
                "$firstNameSharedPref $lastNameSharedPref",
                "$contactSharedPref",
                "$imageSharedPref"
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@AdminActivity, binding.navigationView, isAdminSharedPref)

        setupViewModel()


        binding.btnAddProduct.setOnClickListener{
           setupProductObserver()
           binding.btnAddProduct.text = getString(R.string.loading)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if(isSuccess) {
                    val intent = Intent(this@AdminActivity, ProductCreateUpdateActivity::class.java)
                    intent.putExtra("header", "Add Product")
                    intent.putExtra("idIntent", "$idIntent")
                    intent.putExtra("imageIntent", "$imageIntent")
                    intent.putExtra("nameIntent", "$nameIntent")
                    intent.putExtra("priceIntent", "$priceIntent")
                    intent.putExtra("brandIntent", "$brandIntent")
                    intent.putExtra("descriptionIntent", "$descriptionIntent")
                    intent.putExtra("countInStockIntent", "$countInStockIntent")
                    startActivity(intent)
                    finish()
                }
            }, 1000)
       }

        binding.btnAllProduct.setOnClickListener {
            val intent = Intent(this@AdminActivity, AdminShowAllProductActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val productDao: ProductDAO = OnlineClothingDB.getInstance(application).productDAO
        val productRepository = ProductRepository(productDao)
        val productFactory = ProductViewModelFactory(productRepository)
        productViewModel = ViewModelProvider(this, productFactory).get(ProductViewModel::class.java)

        val categoryDAO: CategoryDAO = OnlineClothingDB.getInstance(application).categoryDAO
        val categoryRepository =  CategoryRepository(categoryDAO)
        val categoryFactory = CategoryViewModelFactory(categoryRepository)
        categoryViewModel = ViewModelProvider(this, categoryFactory).get(CategoryViewModel::class.java)

        val userRepository =  UserRepository()
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)
    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        firstNameSharedPref = sharedPref.getString("firstName", "")
        lastNameSharedPref = sharedPref.getString("lastName", "")
        imageSharedPref = sharedPref.getString("image", "")
        contactSharedPref = sharedPref.getString("contact", "")
        tokenSharedPref = sharedPref.getString("token", "")
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
    }

    /*-------------------------------------OBSERVE PRODUCT API CALL=---------------------------------------------*/
    private fun setupProductObserver() {
        productViewModel.addProduct("Bearer $tokenSharedPref").observe(this@AdminActivity, {
            it.apiProductCall()
        })
    }

    /*-------------------------------------SETUP INTENT DATA-----------------------------------------------------*/
    private fun setupIntentData(data: Product) {
        idIntent = data._id
        nameIntent = data.name
        priceIntent = data.price.toFloat()
        brandIntent = data.brand
        descriptionIntent = data.description
        countInStockIntent = data.countInStock
        imageIntent = data.image
    }

    /*-------------------------------------GET PRODUCT DATA FROM API---------------------------------------------*/
    private fun Resource<ProductDetailResponse>.apiProductCall() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    resource.data?.let { data ->
                        setupIntentData(data.product)
                        isSuccess = data.success
                        Log.i("ADMIN-TAG", "==>LOADED DATA SUCCESSFULLY ==> $data and id is ${idIntent}")
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
    }


    /*-------------------------------------GET CATEGORY DATA FROM API---------------------------------------------*/
    private fun Resource<CategoryResponse>.apiCategoryCall() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    resource.data?.let { data ->
                        Log.i("ADMIN-TAG", "==>LOADED DATA SUCCESSFULLY ==> $data")
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
    }

    /*-------------------------------------GET USER DATA FROM API------------------------------------------------*/
    private fun Resource<UserResponse>.apiUserCall() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    resource.data?.let { data ->

                        Log.i("ADMIN-TAG", "==>LOADED DATA SUCCESSFULLY ==> $data")
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
    }

}