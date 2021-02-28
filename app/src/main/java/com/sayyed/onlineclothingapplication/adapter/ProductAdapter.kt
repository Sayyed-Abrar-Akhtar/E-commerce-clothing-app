package com.sayyed.onlineclothingapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sayyed.onlineclothingapplication.databinding.ProductsLayoutBinding
import com.sayyed.onlineclothingapplication.entities.Product

class ProductAdapter(
        private val productList: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ProductsLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductTitle.text = product.productTitle
            binding.tvProductPrice.text = product.productPrice
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ProductsLayoutBinding.inflate(layoutInflater)

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        holder.bind(productList[position])

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}


