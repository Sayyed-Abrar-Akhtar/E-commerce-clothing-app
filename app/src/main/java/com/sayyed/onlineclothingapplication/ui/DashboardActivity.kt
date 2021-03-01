package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.CategoryAdapter
import com.sayyed.onlineclothingapplication.eventlistener.OnCategoryClickListener
import com.sayyed.onlineclothingapplication.models.Categories

class DashboardActivity : AppCompatActivity(), OnCategoryClickListener {

    private var categoriesList =  ArrayList<Categories>()
    private lateinit var recyclerViewCategory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        recyclerViewCategory = findViewById(R.id.recyclerViewCategory)

        initialCategoryData()

        val adapter = CategoryAdapter(categoriesList, this@DashboardActivity, this)
        recyclerViewCategory.layoutManager = LinearLayoutManager(this@DashboardActivity)
        recyclerViewCategory.adapter = adapter
        adapter.notifyDataSetChanged()

    }




    private fun initialCategoryData() {
        categoriesList.add(
                Categories("New in",
                        "https://image.freepik.com/free-photo/pretty-young-stylish-sexy-woman-pink-luxury-dress-summer-fashion-trend-chic-style-sunglasses-blue-studio-background-shopping-holding-paper-bags-talking-mobile-phone-shopaholic_285396-2957.jpg"
                )
        )


        categoriesList.add(
                Categories("Clothing",
                        "https://image.freepik.com/free-photo/refined-caucasian-girl-blue-denim-jacket-posing_197531-7568.jpg"
                )
        )

        categoriesList.add(
                Categories("Shoes",
                        "https://image.freepik.com/free-photo/cheerful-model-sitting-floor-wearing-modern-oversize-black-jacket-creamy-long-dress-high-heel-shoes-her-feet-curly-hairstyle-makeup_343629-61.jpg"
                )
        )

        categoriesList.add(
                Categories("Accessories",
                        "https://image.freepik.com/free-photo/wonderful-young-woman-with-long-hair-having-fun-rosy-magnificent-girl-trendy-sunglasses-relaxing-during-portraitshoot_197531-11059.jpg"
                )
        )

        categoriesList.add(
                Categories("Trending now",
                        "https://image.freepik.com/free-photo/sexy-brunette-woman-trendy-blue-fur-jacket-velvet-thight-high-boots-posing-grey-wall_273443-4066.jpg"
                )
        )

        categoriesList.add(
                Categories("Active Wear",
                        "https://image.freepik.com/free-photo/good-looking-optimistic-girl-25-years-old-happily-portrait-cute-model-pensive-pose-posing-isolated-wall_197531-12012.jpg"
                )
        )

        categoriesList.add(
                Categories("Face + Body",
                        "https://image.freepik.com/free-photo/full-length-shot-glad-curly-woman-striped-pants-jumping-purple-wall-indoor-portrait-wonderful-girl-sunglasses-fooling-around_197531-5125.jpg"
                )
        )


        categoriesList.add(
                Categories("Brands",
                        "https://image.freepik.com/free-photo/blonde-woman-with-perfect-wavy-hairstyle-pink-party-dress-posing-hight-heels_273443-1636.jpg"
                )
        )
        categoriesList.add(
                Categories("Outlets",
                        "https://image.freepik.com/free-photo/blissful-lady-trendy-summer-clothes-posing-with-camera-yellow-positive-beautiful-girl-hat-chilling-studio_197531-11082.jpg"
                )
        )

        categoriesList.add(
                Categories("Sale",
                        "https://image.freepik.com/free-photo/surprised-happy-girl-pointing-left-recommend-product-advertisement-make-okay-gesture_176420-20191.jpg"
                )
        )

    }

    override fun OnCategoryItemClick(position: Int) {
        Toast.makeText(this, "category -> $position ", Toast.LENGTH_LONG).show()
    }

}