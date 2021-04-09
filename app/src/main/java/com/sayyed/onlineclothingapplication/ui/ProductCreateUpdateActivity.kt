package com.sayyed.onlineclothingapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityProductCreateUpdateBinding
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModel
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModelFactory
import java.util.*

class ProductCreateUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductCreateUpdateBinding
    private lateinit var productViewModel: ProductViewModel


    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle

    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var contactSharedPref : String? = ""
    private  var tokenSharedPref : String? =  ""
    private  var isAdminSharedPref : Boolean = false
    private  var header : String? = ""



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_create_update)

        binding = DataBindingUtil.setContentView(this@ProductCreateUpdateActivity, R.layout.activity_product_create_update)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()

        /*-----------------------GET HEADER DATA FROM ADMIN ACTIVITY THROUGH INTENT-------------------------------*/
        val header = intent.getStringExtra("header")

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

        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "$header")

        navigationDrawerSetup.addHeaderText(
            this@ProductCreateUpdateActivity,
            binding.navigationView,
            "$firstNameSharedPref $lastNameSharedPref",
            "$contactSharedPref",
            "$imageSharedPref",
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@ProductCreateUpdateActivity, binding.navigationView, isAdminSharedPref)

        setupViewModel()

        setAddProductObserver()
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

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val productDao: ProductDAO = OnlineClothingDB.getInstance(application).productDAO
        val repository = ProductRepository(productDao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        productViewModel.insertProductToRoom()
    }

    /*-----------------------------------------CREATE NEW PRODUCT-------------------------------------------------*/
    private fun setAddProductObserver(){
        productViewModel.addProduct("Bearer $tokenSharedPref").observe(this, {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { product ->

                            binding.etProductName.setText(product.name)
                        }
                    }
                }
            }
        })
    }
}