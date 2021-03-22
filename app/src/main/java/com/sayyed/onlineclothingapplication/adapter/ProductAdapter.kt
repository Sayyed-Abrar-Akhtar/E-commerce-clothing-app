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
import com.sayyed.onlineclothingapplication.eventlistener.OnProductClickListener
import com.sayyed.onlineclothingapplication.models.Product

class ProductAdapter(
        val context: Context,
        val productList: MutableList<Product>,
        private val onProductClickListener: OnProductClickListener
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.products_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.tvProductTitle?.text = product.name
        holder.tvProductPrice?.text = product.price.toString()
        holder.tvRating?.text = "${product.rating} stars"

        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(300, 450)

        Glide.with(context)
                .load(product.image)
                .apply(requestOptions)
                .into(holder.imgProduct)

        holder.itemView.setOnClickListener {
            onProductClickListener.OnProductItemClick(position, "${holder.tvProductTitle.text}")
        }
    }

    override fun getItemCount(): Int  = productList.size

    class ProductViewHolder( view: View ) : RecyclerView.ViewHolder(view) {

        var imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        var tvProductTitle: TextView = itemView.findViewById(R.id.tvProductTitle)
        var tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        var tvRating: TextView = itemView.findViewById(R.id.tvRating)

    }
}