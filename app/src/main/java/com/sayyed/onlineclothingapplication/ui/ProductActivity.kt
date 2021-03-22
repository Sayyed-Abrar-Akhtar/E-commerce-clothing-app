package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.database.ProductDB
import com.sayyed.onlineclothingapplication.databinding.ActivityProductBinding
import com.sayyed.onlineclothingapplication.eventlistener.OnProductClickListener
import com.sayyed.onlineclothingapplication.models.Product

import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModel
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModelFactory

class ProductActivity : AppCompatActivity(), OnProductClickListener {




    private lateinit var binding: ActivityProductBinding
    private lateinit var productViewModel: ProductViewModel


    private lateinit var listProduct: MutableList<Product>
    private lateinit var adapter: ProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)

        val categoryName = intent.getStringExtra("categoryName")

        setupUI()
        setupViewModel()
        setupCategorizedProductObservers("$categoryName")


    }


    private fun setupUI() {
        binding.recyclerViewProduct.layoutManager = GridLayoutManager(
                this@ProductActivity, 2, GridLayoutManager.VERTICAL, false)
        listProduct = mutableListOf<Product>()
        adapter = ProductAdapter(this, listProduct, this)
        binding.recyclerViewProduct.adapter = adapter
    }

    private fun setupViewModel() {
        val productDao: ProductDAO = ProductDB.getInstance(application).productDAO
        val repository = ProductRepository(productDao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)


    }

    private fun setupCategorizedProductObservers(category: String) {
        productViewModel.getProductsOfCategory(category).observe(this, {

            it?.let { resource ->
                when (resource.status ) {
                    Status.SUCCESS -> {
                        resource.data?.let { product ->
                            println("---------------------")
                            println("--------------${product.product}")
                            println("---------------------")
                            listProduct.clear()
                            listProduct.addAll(product.product)
                            adapter.notifyDataSetChanged()
                            println("$product")
                            Log.i("productTag", "------------------LOADED FROM API----------------")
                        }
                    }

                    Status.ERROR -> {

                    }

                    Status.LOADING -> {

                    }
                }
            }
        })

    }

    override fun OnProductItemClick(position: Int, product: String) {
        //val intent = Intent(this, DashboardActivity::class.java)
        //intent.putExtra("productid", "id")
    }


}