package com.sayyed.onlineclothingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.eventlistener.OnCategoryClickListener
import com.sayyed.onlineclothingapplication.models.Category
import kotlin.collections.ArrayList

class CategoryAdapter (
        val context: Context,
        val categoryList: MutableList<Category>,
        private val onCategoryClickListener: OnCategoryClickListener
    ): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.categories_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.tvCategoryName?.text = category.name
        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(600, 120)
        Glide.with(context)
                .load(category.image)
                .apply(requestOptions)
                .into(holder.imageViewCategoryImg)


        holder.itemView.setOnClickListener {
            onCategoryClickListener.OnCategoryItemClick(position, "${category.name}", "${category._id}")
        }
    }

    override fun getItemCount(): Int = categoryList.size



    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        var imageViewCategoryImg: ImageView = itemView.findViewById(R.id.imageViewCategoryImg)

    }
}

