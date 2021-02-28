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
import com.sayyed.onlineclothingapplication.entities.Product


class ProductAdapter(
    val productList: ArrayList<Product>,
    val context: Context
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val productTitle: TextView
        val productPrice: TextView
        val productImage: ImageView

        init {
            productTitle = view.findViewById(R.id.tvProductTitle)
            productPrice = view.findViewById(R.id.tvProductPrice)
            productImage = view.findViewById(R.id.imgProduct)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.products_layout, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = productList[position]

        holder.productTitle.text = product.productTitle
        holder.productPrice.text = product.productImage

        Glide.with(context)
            .load(product.productImage)
            .into(holder.productImage)

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}