package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.adapter.ProductAdminAdapter
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityAdminShowAllProductBinding
import com.sayyed.onlineclothingapplication.databinding.ActivityProductBinding
import com.sayyed.onlineclothingapplication.eventlistener.OnAdminProductClickListener
import com.sayyed.onlineclothingapplication.eventlistener.OnProductClickListener
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.response.DeleteResponse
import com.sayyed.onlineclothingapplication.response.ProductResponse
import com.sayyed.onlineclothingapplication.utils.Network
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModel
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModelFactory
import java.util.*

class AdminShowAllProductActivity : AppCompatActivity(), OnAdminProductClickListener {

    private lateinit var binding: ActivityAdminShowAllProductBinding
    private lateinit var productViewModel: ProductViewModel

    private lateinit var listProduct: MutableList<Product>
    private lateinit var adapter: ProductAdminAdapter

    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle

    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var contactSharedPref : String? = ""
    private  var isAdminSharedPref : Boolean = false
    private  var tokenSharedPref : String? =  ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_show_all_product)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_show_all_product)

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
                this@AdminShowAllProductActivity,
                binding.navigationView,
                "$firstNameSharedPref $lastNameSharedPref",
                "$contactSharedPref",
                "$imageSharedPref"
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@AdminShowAllProductActivity, binding.navigationView, isAdminSharedPref)

        /*-----------------------------------RECYCLER VIEW AND ADAPTER SETUP--------------------------------------*/
        setupUI()

        /*----------------------PRODUCT ACTIVITY AND PRODUCT VIEW MODEL CONNECTION--------------------------------*/
        setupViewModel()

        /*------------------------FUNCTION CALLED AND DISPLAYED CATEGORIZED DATA----------------------------------*/
        if (Network.isNetworkAvailable(this)) {
            setupProductObservers()
        } else {
            Toast.makeText(this@AdminShowAllProductActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT ).show()
        }
    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        firstNameSharedPref = sharedPref.getString("firstName", "")
        lastNameSharedPref = sharedPref.getString("lastName", "")
        imageSharedPref = sharedPref.getString("image", "")
        contactSharedPref = sharedPref.getString("contact", "")
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
        tokenSharedPref = sharedPref.getString("token", "")
    }

    /*----------------------CLICK LISTENER ON EDIT PRODUCT IN RECYCLER VIEW----------------------------------------*/
    override fun OnProductEditClick(position: Int, product: Product) {
        val intent = Intent(this@AdminShowAllProductActivity, ProductCreateUpdateActivity::class.java)
        intent.putExtra("header", "Update Product")
        intent.putExtra("idIntent", "${product._id}")
        intent.putExtra("imageIntent", "${product.image}")
        intent.putExtra("nameIntent", "${product.name}")
        intent.putExtra("priceIntent", "${product.price}")
        intent.putExtra("brandIntent", "${product.brand}")
        intent.putExtra("descriptionIntent", "${product.description}")
        intent.putExtra("countInStockIntent", "${product.countInStock}")
        startActivity(intent)
        productViewModel.getAllProducts()
    }

    /*----------------------CLICK LISTENER ON DELETE PRODUCT IN RECYCLER VIEW--------------------------------------*/
    override fun OnProductDeleteClick(position: Int, productId: String) {
        productViewModel.deleteProduct("Bearer $tokenSharedPref", productId).observe(this@AdminShowAllProductActivity, {
            it.loadApiToDelData()
        })
        productViewModel.deleteProductFromRoom(productId)
        adapter.notifyDataSetChanged()
        setupProductObservers()
    }

    /*---------------------------------------------SET UP UI------------------------------------------------------*/
    private fun setupUI() {
        binding.recyclerViewProductAdmin.layoutManager = LinearLayoutManager(this@AdminShowAllProductActivity)
        listProduct = mutableListOf()
        adapter = ProductAdminAdapter(this@AdminShowAllProductActivity, listProduct, this@AdminShowAllProductActivity)
        binding.recyclerViewProductAdmin.adapter = adapter
    }

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val productDao: ProductDAO = OnlineClothingDB.getInstance(application).productDAO
        val repository = ProductRepository(productDao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        productViewModel.insertProductToRoom()
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
                    binding.recyclerViewProductAdmin.visibility = View.VISIBLE

                    resource.data?.let { product ->
                        listProduct.clear()
                        listProduct.addAll(product.product)
                        adapter.notifyDataSetChanged()
                        println("${listProduct}")
                        Log.i("productTag", "==>LOADED PRODUCT DATA FROM API")
                    }
                }

                Status.ERROR -> {
                    binding.recyclerViewProductAdmin.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    println("=========================ERROR====================")
                    println(resource.data)
                    println(resource.message)
                    println("==================================================")
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewProductAdmin.visibility = View.GONE
                    println("=========================LOADER====================")
                    println("!!! LOADING... !!!")
                    println("===================================================")
                }
            }
        }
    }

    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<DeleteResponse>.loadApiToDelData() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewProductAdmin.visibility = View.VISIBLE

                    resource.data?.let { data ->
                        Toast.makeText(this@AdminShowAllProductActivity, data.message, Toast.LENGTH_SHORT).show()
                        Log.i("productDeleteTag", "==>PRODUCT DELETED FROM API")
                    }
                }

                Status.ERROR -> {
                    binding.recyclerViewProductAdmin.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    println("=========================ERROR====================")
                    println(resource.data)
                    println(resource.message)
                    println("==================================================")
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewProductAdmin.visibility = View.GONE
                    println("=========================LOADER====================")
                    println("!!! LOADING... !!!")
                    println("===================================================")
                }
            }
        }
    }


}