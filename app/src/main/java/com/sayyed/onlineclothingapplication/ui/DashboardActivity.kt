package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.CategoryAdapter
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.database.CategoryDB
import com.sayyed.onlineclothingapplication.eventlistener.OnCategoryClickListener
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.utils.Network
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModel
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModelFactory

class DashboardActivity : AppCompatActivity(), OnCategoryClickListener {

    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var dashboardBottomNav: BottomNavigationView
    private lateinit var progressBar: ProgressBar

    private lateinit var listCategory: MutableList<Category>
    private lateinit var adapter: CategoryAdapter
    private lateinit var categoryViewModel: CategoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        recyclerViewCategory = findViewById(R.id.recyclerViewCategory)
        dashboardBottomNav = findViewById(R.id.dashboardBottomNav)
        progressBar = findViewById(R.id.progressBar)
        dashboardBottomNav.setOnNavigationItemSelectedListener(bottomNavigation)

        setupUI()
        setupViewModel()

        loadFromRoomOrAPi()

    }

    private fun loadFromRoomOrAPi() {
        when (Network.isNetworkAvailable(this@DashboardActivity)) {
            true -> setupCategoryObservers()
            false -> loadCategoryFromRoom()
        }
    }


    private fun loadCategoryFromRoom() {
        categoryViewModel.categoryFromRoom.observe(this, {
            it?.let { category ->
                progressBar.visibility = View.GONE
                recyclerViewCategory.visibility = View.VISIBLE
                listCategory.clear()
                listCategory.addAll(category)
                adapter.notifyDataSetChanged()
                Log.i("CategoryTAG", "------------------LOADED FROM ROOM----------------")
            }
        })
    }

    private fun setupCategoryObservers() {
        categoryViewModel.getCategory().observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        recyclerViewCategory.visibility = View.VISIBLE

                        resource.data?.let { category ->
                            listCategory.clear()
                            listCategory.addAll(category.category)
                            adapter.notifyDataSetChanged()
                            categoryViewModel.deleteAllCategory()
                            Log.i("CategoryTAG", "------------------LOADED FROM API----------------")
                        }
                    }

                    Status.ERROR -> {
                        recyclerViewCategory.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }

                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerViewCategory.visibility = View.GONE
                    }
                }

            }
        })

    }


    private fun setupUI() {
        recyclerViewCategory.layoutManager =LinearLayoutManager(this@DashboardActivity)
        listCategory = mutableListOf<Category>()
        adapter =CategoryAdapter(this, listCategory, this)
        recyclerViewCategory.adapter = adapter
    }

    private fun setupViewModel() {
        val categoryDAO: CategoryDAO = CategoryDB.getInstance(application).categoryDAO
        val repository =  CategoryRepository(categoryDAO)
        val factory =CategoryViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, factory).get(CategoryViewModel::class.java)
        categoryViewModel.insertCategoryIntoRoom()
    }

    override fun OnCategoryItemClick(position: Int, category: String) {
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
    }

    private val bottomNavigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.dashboard -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.cart -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.info -> {
                val intent = Intent(this@DashboardActivity, InformationActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }






}