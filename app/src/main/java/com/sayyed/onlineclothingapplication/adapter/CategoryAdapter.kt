package com.sayyed.onlineclothingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.eventlistener.OnCategoryClickListener
import com.sayyed.onlineclothingapplication.models.Categories
import kotlin.collections.ArrayList

class CategoryAdapter (
    val categoriesList: ArrayList<Categories>,
    val context: Context,
    private val onCategoryClickListener: OnCategoryClickListener
    ): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categories_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoriesList[position]
        holder.categoryName.text = category.categoryName

        Glide.with(context)
                .load(category.categoryImage)
                .into(holder.categoryImage)

        holder.itemView.setOnClickListener {
            onCategoryClickListener.OnCategoryItemClick(position, "${holder.categoryName.text}")
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }


    inner class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val categoryName : TextView
        val categoryImage : ImageView

        init {
            categoryName = view.findViewById(R.id.tvCategoryName)
            categoryImage = view.findViewById(R.id.imageViewCategoryImg)

        }




    }


}

