package com.sayyed.onlineclothingapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.database.ProductDB
import com.sayyed.onlineclothingapplication.databinding.ActivityProductBinding
import com.sayyed.onlineclothingapplication.entities.Product
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModel
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModelFactory

class ProductActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProductBinding
    private lateinit var productViewModel: ProductViewModel

    private lateinit var recyclerViewProduct: RecyclerView
    private var productsList =  ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        val dao: ProductDAO = ProductDB.getInstance(application).productDAO
        val repository = ProductRepository(dao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        binding.productViewModel = productViewModel
        binding.lifecycleOwner = this

        displayProductList()

        recyclerViewProduct = findViewById(R.id.recyclerViewProduct)


        val adapter = ProductAdapter(productsList, this@ProductActivity)
        recyclerViewProduct.layoutManager = LinearLayoutManager(this@ProductActivity)
        recyclerViewProduct.adapter = adapter
    }

    private fun displayProductList() {
        productViewModel.products.observe(this, Observer {
            Log.i("MYTAG", it.toString())
        })
    }

    private fun allProducts() {
        productsList.add(
            Product(
                "Blue T-shirt",
            "NPR 1500",
                "100% cotton",
                "Blue",
                "Small",
                "https://cdn.pixabay.com/photo/2013/07/13/14/07/apparel-162180_960_720.png"
            )
        )

        productsList.add(
            Product(
                "Blue Pant",
                "NPR 3500",
                "100% cotton",
                "Blue",
                "Large",
                "https://cdn.pixabay.com/photo/2014/04/02/10/40/jeans-304196_960_720.png"
            )
        )

        productsList.add(
            Product(
                "Baby Shoe",
                "NPR 6000",
                "100% Leather",
                "Green",
                "Small",
                "https://cdn.pixabay.com/photo/2014/10/27/19/18/baby-shoes-505471_960_720.jpg"
            )
        )

        productsList.add(
            Product(
                "Woollen Shoe",
                "NPR 6900",
                "100% Wool",
                "Pink",
                "Small",
                "https://cdn.pixabay.com/photo/2014/10/27/19/18/baby-shoes-505471_960_720.jpg"
            )
        )

        productsList.add(
            Product(
                "Baby Scarf",
                "NPR 799",
                "100% Wool",
                "Grey",
                "Small",
                "https://cdn.pixabay.com/photo/2015/12/21/05/45/girl-1102086_960_720.jpg"
            )
        )

        productsList.add(
            Product(
                "Sweater",
                "NPR 3500",
                "100% wool",
                "Navy",
                "Small",
                "https://cdn.pixabay.com/photo/2016/03/27/19/31/fashion-1283863_960_720.jpg"
            )
        )

        productsList.add(
            Product(
                "Baby Hat",
                "NPR 1500",
                "100% Polyester",
                "Stripe",
                "Small",
                "https://cdn.pixabay.com/photo/2016/05/17/22/16/baby-1399332_960_720.jpg"
            )
        )
    }


}