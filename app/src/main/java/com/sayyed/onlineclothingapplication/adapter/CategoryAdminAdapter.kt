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
import com.sayyed.onlineclothingapplication.eventlistener.OnAdminCategoryClickListener
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.utils.FileUpload

class CategoryAdminAdapter (
        val context: Context,
        private val categoryList: MutableList<Category>,
        private val onAdminCategoryClickListener: OnAdminCategoryClickListener
): RecyclerView.Adapter<CategoryAdminAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.show_all_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.tvItemName.text = category.name

        holder.tvItemQty.visibility = View.GONE

        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(600, 120)
        Glide.with(context)
                .load(FileUpload.checkImageString(category.image))
                .apply(requestOptions)
                .into(holder.imgProductItem)

        holder.ivItemEdit.setOnClickListener {
            onAdminCategoryClickListener.onProductEditClick(position, category)
        }

        holder.ivItemDelete.setOnClickListener {
            onAdminCategoryClickListener.onProductDeleteClick(position, "${category._id}")
        }

    }

    override fun getItemCount(): Int = categoryList.size



    class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        var tvItemQty: TextView = itemView.findViewById(R.id.tvItemQty)
        var imgProductItem: ImageView = itemView.findViewById(R.id.imgProductItem)
        var ivItemEdit: ImageView = itemView.findViewById(R.id.ivItemEdit)
        var ivItemDelete: ImageView = itemView.findViewById(R.id.ivItemDelete)

    }
}