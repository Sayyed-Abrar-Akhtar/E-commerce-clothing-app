package com.sayyed.onlineclothingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.eventlistener.OnAdminProductClickListener
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.utils.FileUpload

class ProductAdminAdapter (
        val context: Context,
        val productList: MutableList<Product>,
        private val onAdminProductClickListener: OnAdminProductClickListener
): RecyclerView.Adapter<ProductAdminAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ProductViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.show_all_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.tvItemName?.text = product.name
        holder.tvItemQty?.text = "Rs${product.price} - ${product.countInStock} items"

        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(300, 450)

        Glide.with(context)
                .load(FileUpload.checkImageString(product.image))
                .apply(requestOptions)
                .into(holder.imgProductItem)

        holder.ivItemEdit.setOnClickListener {
            onAdminProductClickListener.OnProductEditClick(position, product)
        }

        holder.ivItemDelete.setOnClickListener {
            onAdminProductClickListener.OnProductDeleteClick(position, "${product._id}")
        }
    }

    override fun getItemCount(): Int = productList.size

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgProductItem: ImageView = itemView.findViewById(R.id.imgProductItem)
        var tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        var tvItemQty: TextView = itemView.findViewById(R.id.tvItemQty)
        var ivItemEdit: ImageView = itemView.findViewById(R.id.ivItemEdit)
        var ivItemDelete: ImageView = itemView.findViewById(R.id.ivItemDelete)
    }
}