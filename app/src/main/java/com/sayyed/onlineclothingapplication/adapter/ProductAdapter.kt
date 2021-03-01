package com.sayyed.onlineclothingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sayyed.onlineclothingapplication.databinding.ProductsLayoutBinding
import com.sayyed.onlineclothingapplication.entities.Product
import kotlinx.coroutines.withContext

class ProductAdapter(
        private val productList: List<Product>,
        private val context: Context
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val context: Context, val binding: ProductsLayoutBinding): RecyclerView.ViewHolder(binding.root) {




        fun bind( product: Product) {
            binding.tvProductTitle.text = product.productTitle
            binding.tvProductPrice.text = product.productPrice

            Glide.with(context)
                    .load(product.productImage)
                    .into(binding.imgProduct)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ProductsLayoutBinding.inflate(layoutInflater)

        return ProductViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {


        holder.bind(productList[position])

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}


