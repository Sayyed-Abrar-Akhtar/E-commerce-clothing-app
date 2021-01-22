package com.sayyed.onlineclothingapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.sayyed.onlineclothingapplication.adapter.CategoryAdapter
import com.sayyed.onlineclothingapplication.models.Categories

class DashboardActivity : AppCompatActivity() {

    private var categoriesList: ArrayList<Categories>()
    private lateinit var recyclerViewCategory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        recyclerViewCategory = findViewById(R.id.recyclerViewCategory)

        initialCategoryData()

        val adapter = CategoryAdapter(categoriesList, this@DashboardActivity)
        recyclerViewCategory.adapter = adapter

    }

    private fun initialCategoryData() {
        TODO("Not yet implemented")
    }
}