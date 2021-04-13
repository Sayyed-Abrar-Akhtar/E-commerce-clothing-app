package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.CategoryAdapter
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityDashboardBinding
import com.sayyed.onlineclothingapplication.eventlistener.OnCategoryClickListener
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.response.CategoryResponse
import com.sayyed.onlineclothingapplication.utils.Network
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModel
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModelFactory
import java.lang.Exception

class DashboardActivity : AppCompatActivity(), OnCategoryClickListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle


    private lateinit var listCategory: MutableList<Category>
    private lateinit var adapter: CategoryAdapter
    private lateinit var categoryViewModel: CategoryViewModel


    private lateinit var firstNameSharedPref : String
    private lateinit var lastNameSharedPref : String
    private lateinit var imageSharedPref : String
    private lateinit var contactSharedPref : String
    private  var isAdminSharedPref : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/


        firstNameSharedPref = intent.getStringExtra("name").toString()
        imageSharedPref = intent.getStringExtra("image").toString()
        contactSharedPref = intent.getStringExtra("contact").toString()

        getSharedPref()

        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
                this@DashboardActivity,
                binding.drawer,
                binding.toolbar,
                R.string.open,
                R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*----------------------------------------NAVIGATION DRAWER LAYOUT----------------------------------------*/
        navigationDrawerSetup = NavigationDrawerSetup()
        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "Online Clothing")
        navigationDrawerSetup.addHeaderText(
                this@DashboardActivity,
                binding.navigationView,
                "$firstNameSharedPref $lastNameSharedPref",
                "$contactSharedPref",
                "$imageSharedPref"
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@DashboardActivity, binding.navigationView, isAdminSharedPref)

        setupUI()

        setupViewModel()

        /*----------------------------------------ASK FOR PERMISSIONS---------------------------------------------*/
        if (!hasPermission()) {
            requestPermission()
        }

        when (Network.isNetworkAvailable(this@DashboardActivity)) {
            true -> setupCategoryObservers()
            false -> {
                Toast.makeText(this@DashboardActivity, "No internet connection!!", Toast.LENGTH_SHORT).show()
                try {
                    loadCategoryFromRoom()
                } catch (ex: Exception) {
                    println("Error loading category data from database. ==> $ex")
                }
            }
        }
    }

    /*-------------------------------------REQUEST PERMISSIONS----------------------------------------------------*/
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
                this@DashboardActivity,
                permissions, 234
        )
    }

    /*-------------------------------------CHECK Permissions------------------------------------------------------*/
    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                            this,
                            permission
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
            }
        }
        return hasPermission
    }

    /*-------------------------------------ASK PERMISSIONS--------------------------------------------------------*/
    private val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        firstNameSharedPref = sharedPref.getString("firstName", "").toString()
        lastNameSharedPref = sharedPref.getString("lastName", "").toString()
        imageSharedPref = sharedPref.getString("image", "").toString()
        contactSharedPref = sharedPref.getString("contact", "").toString()
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
    }


    /*---------------------------------------------SET UP UI------------------------------------------------------*/
    private fun setupUI() {
        binding.recyclerViewCategory.layoutManager =LinearLayoutManager(this@DashboardActivity)
        listCategory = mutableListOf()
        adapter =CategoryAdapter(this, listCategory, this)
        binding.recyclerViewCategory.adapter = adapter
    }

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val categoryDAO: CategoryDAO = OnlineClothingDB.getInstance(application).categoryDAO
        val repository =  CategoryRepository(categoryDAO)
        val factory = CategoryViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, factory).get(CategoryViewModel::class.java)
        categoryViewModel.insertCategoryIntoRoom()
    }

    /*----------------------------------EVENT LISTENER ON CATEGORY ITEM CLICK-------------------------------------*/
    override fun OnCategoryItemClick(position: Int, categoryName: String, categoryId:String) {
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("categoryName", categoryName)
        intent.putExtra("categoryId", categoryId)
        startActivity(intent)
    }



    /*-------------------------------------GET DATA FROM ROOM TO DISPLAY------------------------------------------*/
    private fun loadCategoryFromRoom() {
        try {
            categoryViewModel.categoryFromRoom.observe(this@DashboardActivity, {
                it?.let { category ->
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewCategory.visibility = View.VISIBLE
                    listCategory.clear()
                    listCategory.addAll(category)
                    adapter.notifyDataSetChanged()
                    Log.i("CategoryTAG", "==>LOADED CATEGORY DATA FROM ROOM")
                    println(category)
                }
            })
        } catch (ex: Exception) {
            println(ex)
        }
    }

    /*-------------------------------------SET DATA FROM API TO DISPLAY-------------------------------------------*/
    private fun setupCategoryObservers() {
        categoryViewModel.getCategory().observe(this, {
            it.loadApiData()
        })
    }

    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<CategoryResponse>.loadApiData() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewCategory.visibility = View.VISIBLE

                    resource.data?.let { category ->
                        listCategory.clear()
                        listCategory.addAll(category.category)
                        adapter.notifyDataSetChanged()
                        categoryViewModel.deleteAllCategory()
                        println(category)
                        Log.i("CategoryTAG", "==>LOADED CATEGORY DATA FROM API")
                    }
                }
                Status.ERROR -> {
                    binding.recyclerViewCategory.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    println("=========================ERROR====================")
                    println(resource.data)
                    println(resource.message)
                    println("==================================================")
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewCategory.visibility = View.GONE
                    println("=========================LOADER====================")
                    println("!!! LOADING... !!!")
                    println("===================================================")
                }

            }
        }
    }
}