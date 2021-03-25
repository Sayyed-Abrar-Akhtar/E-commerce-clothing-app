package com.sayyed.onlineclothingapplication.ui

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
import com.sayyed.onlineclothingapplication.database.ProductDB
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

class ProductActivity : AppCompatActivity(), OnProductClickListener {


    private lateinit var binding: ActivityProductBinding
    private lateinit var productViewModel: ProductViewModel

    private lateinit var listProduct: MutableList<Product>
    private lateinit var adapter: ProductAdapter

    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)

        /*-----------------------GET CATEGORY DATA FROM DASHBOARD ACTIVITY THROUGH INTENT-------------------------*/
        val categoryName = intent.getStringExtra("categoryName")

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
                "",
                "",
                "https://i.pinimg.com/280x280_RS/45/57/31/455731391ed7c0b084f935d32a0f2612.jpg")
        navigationDrawerSetup.addEventListenerToNavItems(this@ProductActivity, binding.navigationView)

        /*-----------------------------------RECYCLER VIEW AND ADAPTER SETUP--------------------------------------*/
        setupUI()

        /*----------------------PRODUCT ACTIVITY AND PRODUCT VIEW MODEL CONNECTION--------------------------------*/
        setupViewModel()

        /*------------------------FUNCTION CALLED AND DISPLAYED CATEGORIZED DATA----------------------------------*/
        when (Network.isNetworkAvailable(this)) {
            true -> {
                if (categoryName === "" || categoryName === null) {
                    setupProductObservers()
                } else {
                    setupCategorizedProductObservers(categoryName)
                }
            }
            false -> {
                if (categoryName === "" || categoryName === null) {
                    loadProductFromRoom()
                } else {
                    loadCategorisedProductFromRoom(categoryName)
                }
            }
        }
    }

    /*----------------------CLICK LISTENER ON PRODUCTS IN RECYCLER VIEW-------------------------------------------*/
    override fun OnProductItemClick(position: Int, product: String) {
        //val intent = Intent(this, DashboardActivity::class.java)
        //intent.putExtra("productid", "id")
    }

    /*---------------------------------------------SET UP UI------------------------------------------------------*/
    private fun setupUI() {
        binding.recyclerViewProduct.layoutManager = GridLayoutManager(
                this@ProductActivity, 2, GridLayoutManager.VERTICAL, false)
        listProduct = mutableListOf()
        adapter = ProductAdapter(this, listProduct, this)
        binding.recyclerViewProduct.adapter = adapter
    }

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val productDao: ProductDAO = ProductDB.getInstance(application).productDAO
        val repository = ProductRepository(productDao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        productViewModel.insertProductToRoom()
    }

    /*-------------------------------------CHECK NETWORK TO DISPLAY DATA------------------------------------------*/
    private fun loadFromRoomOrApi(categoryName: String) {
        when (Network.isNetworkAvailable(this)) {
            true -> {
                if (categoryName === "" || categoryName === null) {
                    setupProductObservers()
                } else {
                    setupCategorizedProductObservers(categoryName)
                }
            }
            false -> {
                if (categoryName === "" || categoryName === null) {
                    loadProductFromRoom()
                } else {
                    loadCategorisedProductFromRoom(categoryName)
                }
            }
        }
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
    private fun setupCategorizedProductObservers(category: String) {
        productViewModel.getProductsOfCategory(category).observe(this, {
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
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewProduct.visibility = View.GONE
                }
            }
        }
    }
}