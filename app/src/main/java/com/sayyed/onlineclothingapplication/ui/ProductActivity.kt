package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityProductBinding
import com.sayyed.onlineclothingapplication.eventlistener.OnProductClickListener
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.response.ProductResponse
import com.sayyed.onlineclothingapplication.utils.Network
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModel
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModelFactory
import java.util.*

class ProductActivity : AppCompatActivity(), OnProductClickListener, SensorEventListener {


    private lateinit var binding: ActivityProductBinding
    private lateinit var productViewModel: ProductViewModel

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null


    private lateinit var listProduct: MutableList<Product>
    private lateinit var adapter: ProductAdapter

    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle

    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var contactSharedPref : String? = ""
    private  var isAdminSharedPref : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager


        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()

        /*-----------------------GET CATEGORY DATA FROM DASHBOARD ACTIVITY THROUGH INTENT-------------------------*/
        val categoryName = intent.getStringExtra("categoryName")
        val categoryId = intent.getStringExtra("categoryId")

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
        if (categoryName === null || categoryName === "") { // --check if showing categorised products or all products--
            navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "All Products")
        } else {
            navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, categoryName.capitalize(Locale.ROOT))
        }
        navigationDrawerSetup.addHeaderText(
                this@ProductActivity,
                binding.navigationView,
                "$firstNameSharedPref $lastNameSharedPref",
                "$contactSharedPref",
                "$imageSharedPref"
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@ProductActivity, binding.navigationView, isAdminSharedPref)

        /*-----------------------------------RECYCLER VIEW AND ADAPTER SETUP--------------------------------------*/
        setupUI()

        /*----------------------PRODUCT ACTIVITY AND PRODUCT VIEW MODEL CONNECTION--------------------------------*/
        setupViewModel()

        /*---------------------------------------SENSORS----------------------------------------------------------*/
        if(!checkLightSensor()) {
            return
        } else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        /*------------------------FUNCTION CALLED AND DISPLAYED CATEGORIZED DATA----------------------------------*/
        when (Network.isNetworkAvailable(this)) {
            true -> {
                if (categoryId === "" || categoryId === null) {
                    setupProductObservers()
                } else {
                    setupCategorizedProductObservers("$categoryId")
                }
            }
            false -> {
                if (categoryId === "" || categoryId === null) {
                    loadProductFromRoom()
                } else {
                    loadCategorisedProductFromRoom("$categoryId")
                }
            }
        }
    }

    /*----------------------------------LIGHT SENSOR---------------------------------------------------------*/
    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]
        if(values <= 1000 ){
            binding.recyclerViewProduct.setBackgroundColor(0xFF222222.toInt())

        } else {
            binding.recyclerViewProduct.setBackgroundColor(Color.WHITE)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun checkLightSensor(): Boolean {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            flag = false
        }
        return flag
    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        firstNameSharedPref = sharedPref.getString("firstName", "")
        lastNameSharedPref = sharedPref.getString("lastName", "")
        imageSharedPref = sharedPref.getString("image", "")
        contactSharedPref = sharedPref.getString("contact", "")
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
    }

    /*----------------------CLICK LISTENER ON PRODUCTS IN RECYCLER VIEW-------------------------------------------*/
    override fun OnProductItemClick(position: Int, product: String) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("product_id", product)
        startActivity(intent)
    }

    /*---------------------------------------------SET UP UI------------------------------------------------------*/
    private fun setupUI() {
        binding.recyclerViewProduct.layoutManager = GridLayoutManager(
                this@ProductActivity, 2, GridLayoutManager.VERTICAL, false)
        listProduct = mutableListOf()
        adapter = ProductAdapter(this@ProductActivity, listProduct, this@ProductActivity)
        binding.recyclerViewProduct.adapter = adapter
    }

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val productDao: ProductDAO = OnlineClothingDB.getInstance(application).productDAO
        val repository = ProductRepository(productDao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        productViewModel.insertProductToRoom()
    }


    /*----------------------------------GET PRODUCT FROM ROOM TO DISPLAY------------------------------------------*/
    private fun loadProductFromRoom() {
        productViewModel.retrieveProductsFromRoom.observe(this, {
            it?.let { product ->
                binding.recyclerViewProduct.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                listProduct.clear()
                listProduct.addAll(product)
                adapter.notifyDataSetChanged()
                Log.i("ProductTAG", "==>LOADED PRODUCT DATA FROM ROOM")
            }
        })
    }

    /*-----------------------GET CATEGORISED PRODUCT FROM ROOM TO DISPLAY------------------------------------------*/
    private fun loadCategorisedProductFromRoom(category: String) {
        productViewModel.retrieveCategorizedProductsFromRoom(category).observe(this, {
            it?.let { product ->
                binding.recyclerViewProduct.visibility = View. VISIBLE
                binding.progressBar.visibility = View.GONE
                listProduct.clear()
                listProduct.addAll(product)
                adapter.notifyDataSetChanged()
                Log.i("ProductTAG", "==>LOADED CATEGORIZED PRODUCT DATA FROM ROOM")
            }
        })
    }

    /*-------------------------------------SET DATA FROM API TO DISPLAY-------------------------------------------*/
    private fun setupCategorizedProductObservers(categoryId: String) {
        productViewModel.getProductsOfCategory(categoryId).observe(this, {
            it.loadApiData()
        })
    }

    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun setupProductObservers() {
        productViewModel.getAllProducts().observe(this, {
            it.loadApiData()
        })
    }

    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<ProductResponse>.loadApiData() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewProduct.visibility = View.VISIBLE

                    resource.data?.let { product ->
                        listProduct.clear()
                        listProduct.addAll(product.product)
                        adapter.notifyDataSetChanged()
                        println("${listProduct}")
                        Log.i("productTag", "==>LOADED PRODUCT DATA FROM API")
                    }
                }

                Status.ERROR -> {
                    binding.recyclerViewProduct.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    println("=========================ERROR====================")
                    println(resource.data)
                    println(resource.message)
                    println("==================================================")
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewProduct.visibility = View.GONE
                    println("=========================LOADER====================")
                    println("!!! LOADING... !!!")
                    println("===================================================")
                }
            }
        }
    }
}