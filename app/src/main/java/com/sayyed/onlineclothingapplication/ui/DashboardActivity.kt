package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.CategoryAdapter
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.database.CategoryDB
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

class DashboardActivity : AppCompatActivity(), OnCategoryClickListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle


    private lateinit var listCategory: MutableList<Category>
    private lateinit var adapter: CategoryAdapter
    private lateinit var categoryViewModel: CategoryViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

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
                "Sayyed",
                "+977-9821117484",
                "https://i.pinimg.com/280x280_RS/45/57/31/455731391ed7c0b084f935d32a0f2612.jpg")
        navigationDrawerSetup.addEventListenerToNavItems(this@DashboardActivity, binding.navigationView)

        setupUI()

        setupViewModel()

        loadFromRoomOrAPi()

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
        val categoryDAO: CategoryDAO = CategoryDB.getInstance(application).categoryDAO
        val repository =  CategoryRepository(categoryDAO)
        val factory = CategoryViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, factory).get(CategoryViewModel::class.java)
        categoryViewModel.insertCategoryIntoRoom()
    }

    /*----------------------------------EVENT LISTENER ON CATEGORY ITEM CLICK-------------------------------------*/
    override fun OnCategoryItemClick(position: Int, category: String) {
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("categoryName", category)
        startActivity(intent)
    }

    /*-------------------------------------CHECK NETWORK TO DISPLAY DATA------------------------------------------*/
    private fun loadFromRoomOrAPi() {
        when (Network.isNetworkAvailable(this@DashboardActivity)) {
            true -> setupCategoryObservers()
            false -> loadCategoryFromRoom()
        }
    }

    /*-------------------------------------GET DATA FROM ROOM TO DISPLAY------------------------------------------*/
    private fun loadCategoryFromRoom() {
        categoryViewModel.categoryFromRoom.observe(this, {
            it?.let { category ->
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewCategory.visibility = View.VISIBLE
                listCategory.clear()
                listCategory.addAll(category)
                adapter.notifyDataSetChanged()
                Log.i("CategoryTAG", "==>LOADED PRODUCT DATA FROM ROOM")
            }
        })
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
                        Log.i("CategoryTAG", "==>LOADED PRODUCT DATA FROM API")
                    }
                }
                Status.ERROR -> {
                    binding.recyclerViewCategory.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewCategory.visibility = View.GONE
                }
            }
        }
    }
}