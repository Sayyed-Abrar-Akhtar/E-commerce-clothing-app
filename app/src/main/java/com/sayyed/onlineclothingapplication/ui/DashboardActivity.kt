package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import android.os.Bundle
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
        setupCategoryObservers()
        loadCategoryFromRoom()


    }

    private fun loadCategoryFromRoom() {
        categoryViewModel.categoryFromRoom.observe(this, {
            it?.let { category ->
                progressBar.visibility = View.GONE
                recyclerViewCategory.visibility = View.VISIBLE
                listCategory.clear()
                listCategory.addAll(category)
                adapter.notifyDataSetChanged()
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



    /*private fun initialCategoryData() {
        categoriesList.add(
            Category("New in",
                "https://image.freepik.com/free-photo/pretty-young-stylish-sexy-woman-pink-luxury-dress-summer-fashion-trend-chic-style-sunglasses-blue-studio-background-shopping-holding-paper-bags-talking-mobile-phone-shopaholic_285396-2957.jpg"
            )
        )


        categoriesList.add(
            Category("Clothing",
                "https://image.freepik.com/free-photo/refined-caucasian-girl-blue-denim-jacket-posing_197531-7568.jpg"
            )
        )

        categoriesList.add(
            Category("Shoes",
                "https://image.freepik.com/free-photo/cheerful-model-sitting-floor-wearing-modern-oversize-black-jacket-creamy-long-dress-high-heel-shoes-her-feet-curly-hairstyle-makeup_343629-61.jpg"
            )
        )

        categoriesList.add(
            Category("Accessories",
                "https://image.freepik.com/free-photo/wonderful-young-woman-with-long-hair-having-fun-rosy-magnificent-girl-trendy-sunglasses-relaxing-during-portraitshoot_197531-11059.jpg"
            )
        )

        categoriesList.add(
            Category("Trending now",
                "https://image.freepik.com/free-photo/sexy-brunette-woman-trendy-blue-fur-jacket-velvet-thight-high-boots-posing-grey-wall_273443-4066.jpg"
            )
        )

        categoriesList.add(
            Category("Active Wear",
                "https://image.freepik.com/free-photo/good-looking-optimistic-girl-25-years-old-happily-portrait-cute-model-pensive-pose-posing-isolated-wall_197531-12012.jpg"
            )
        )

        categoriesList.add(
            Category("Face + Body",
                "https://image.freepik.com/free-photo/full-length-shot-glad-curly-woman-striped-pants-jumping-purple-wall-indoor-portrait-wonderful-girl-sunglasses-fooling-around_197531-5125.jpg"
            )
        )


        categoriesList.add(
            Category("Brands",
                "https://image.freepik.com/free-photo/blonde-woman-with-perfect-wavy-hairstyle-pink-party-dress-posing-hight-heels_273443-1636.jpg"
            )
        )
        categoriesList.add(
            Category("Outlets",
                "https://image.freepik.com/free-photo/blissful-lady-trendy-summer-clothes-posing-with-camera-yellow-positive-beautiful-girl-hat-chilling-studio_197531-11082.jpg"
            )
        )

        categoriesList.add(
            Category("Sale",
                "https://image.freepik.com/free-photo/surprised-happy-girl-pointing-left-recommend-product-advertisement-make-okay-gesture_176420-20191.jpg"
            )
        )

    }*/


}