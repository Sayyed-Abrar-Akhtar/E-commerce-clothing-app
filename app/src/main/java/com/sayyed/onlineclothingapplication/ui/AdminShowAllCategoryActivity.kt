package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.CategoryAdapter
import com.sayyed.onlineclothingapplication.adapter.CategoryAdminAdapter
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityAdminShowAllCategoryBinding
import com.sayyed.onlineclothingapplication.eventlistener.OnAdminCategoryClickListener
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.response.CategoryResponse
import com.sayyed.onlineclothingapplication.response.DeleteResponse
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModel
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModelFactory


class AdminShowAllCategoryActivity : AppCompatActivity(), OnAdminCategoryClickListener {

    private lateinit var binding: ActivityAdminShowAllCategoryBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle


    private lateinit var listCategory: MutableList<Category>
    private lateinit var adapter: CategoryAdminAdapter
    private lateinit var categoryViewModel: CategoryViewModel


    private lateinit var firstNameSharedPref : String
    private lateinit var lastNameSharedPref : String
    private lateinit var imageSharedPref : String
    private lateinit var contactSharedPref : String
    private  var isAdminSharedPref : Boolean = false
    private  var tokenSharedPref : String? =  ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_show_all_category)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_show_all_category)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/


        firstNameSharedPref = intent.getStringExtra("name").toString()
        imageSharedPref = intent.getStringExtra("image").toString()
        contactSharedPref = intent.getStringExtra("contact").toString()

        getSharedPref()

        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
                this@AdminShowAllCategoryActivity,
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
                this@AdminShowAllCategoryActivity,
                binding.navigationView,
                "$firstNameSharedPref $lastNameSharedPref",
                "$contactSharedPref",
                "$imageSharedPref"
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@AdminShowAllCategoryActivity, binding.navigationView, isAdminSharedPref)

        setupUI()

        setupViewModel()

        /*----------------------------------------ASK FOR PERMISSIONS---------------------------------------------*/
        setupCategoryObservers()

    }

    override fun onProductEditClick(position: Int, category: Category) {
        val intent = Intent(this@AdminShowAllCategoryActivity, CategoryCreateUpdateActivity::class.java)
        intent.putExtra("header", "Update Category")
        intent.putExtra("idIntent", "${category._id}")
        intent.putExtra("nameIntent", "${category.name}")
        intent.putExtra("imageIntent", "${category.image}")
        startActivity(intent)
        finish()


    }

    override fun onProductDeleteClick(position: Int, categoryId: String) {
        categoryViewModel.deleteCategory("Bearer $tokenSharedPref", categoryId).observe(this@AdminShowAllCategoryActivity, {
            it.loadApiToDelData()
        })
        categoryViewModel.deleteCategoryFromRoom(categoryId)
        val handler = Handler(Looper.getMainLooper())
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewCategoryAdmin.visibility = View.GONE
        handler.postDelayed({
            setupCategoryObservers()
            binding.progressBar.visibility = View.GONE
            binding.recyclerViewCategoryAdmin.visibility = View.VISIBLE
        }, 1000)
    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        firstNameSharedPref = sharedPref.getString("firstName", "").toString()
        lastNameSharedPref = sharedPref.getString("lastName", "").toString()
        imageSharedPref = sharedPref.getString("image", "").toString()
        contactSharedPref = sharedPref.getString("contact", "").toString()
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
        tokenSharedPref = sharedPref.getString("token", "")
    }


    /*---------------------------------------------SET UP UI------------------------------------------------------*/
    private fun setupUI() {
        binding.recyclerViewCategoryAdmin.layoutManager = LinearLayoutManager(this@AdminShowAllCategoryActivity)
        listCategory = mutableListOf()
        adapter = CategoryAdminAdapter(this, listCategory, this)
        binding.recyclerViewCategoryAdmin.adapter = adapter
    }

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val categoryDAO: CategoryDAO = OnlineClothingDB.getInstance(application).categoryDAO
        val repository =  CategoryRepository(categoryDAO)
        val factory = CategoryViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, factory).get(CategoryViewModel::class.java)
        categoryViewModel.insertCategoryIntoRoom()
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
                    binding.recyclerViewCategoryAdmin.visibility = View.VISIBLE

                    resource.data?.let { category ->
                        listCategory.clear()
                        listCategory.addAll(category.category)
                        adapter.notifyDataSetChanged()
                        Log.i("CategoryTAG", "==>LOADED CATEGORY DATA FROM API")
                    }
                }
                Status.ERROR -> {
                    binding.recyclerViewCategoryAdmin.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    println("=========================ERROR====================")
                    println(resource.data)
                    println(resource.message)
                    println("==================================================")
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewCategoryAdmin.visibility = View.GONE
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
                    binding.recyclerViewCategoryAdmin.visibility = View.VISIBLE

                    resource.data?.let { data ->
                        Toast.makeText(this@AdminShowAllCategoryActivity, data.message, Toast.LENGTH_SHORT).show()
                        Log.i("productDeleteTag", "==>PRODUCT DELETED FROM API")
                    }
                }

                Status.ERROR -> {
                    binding.recyclerViewCategoryAdmin.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    println("=========================ERROR====================")
                    println(resource.data)
                    println(resource.message)
                    println("==================================================")
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewCategoryAdmin.visibility = View.GONE
                    println("=========================LOADER====================")
                    println("!!! LOADING... !!!")
                    println("===================================================")
                }
            }
        }
    }
}