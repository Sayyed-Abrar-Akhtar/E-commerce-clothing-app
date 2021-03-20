package com.sayyed.onlineclothingapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.database.ProductDB
import com.sayyed.onlineclothingapplication.databinding.ActivityProductBinding

import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModel
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModelFactory

class ProductActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProductBinding
    private lateinit var productViewModel: ProductViewModel


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


        productViewModel.addProduct()
        initRecyclerView()


    }

    private fun initRecyclerView() {

        binding.recyclerViewProduct.layoutManager = GridLayoutManager(
                this, 2, GridLayoutManager.VERTICAL, false)

        displayProductList()
    }

    private fun displayProductList() {
        productViewModel.products.observe(this, Observer {
            Log.i("MYTAG", it.toString())
            binding.recyclerViewProduct.adapter = ProductAdapter(it, this@ProductActivity)
        })
    }




}