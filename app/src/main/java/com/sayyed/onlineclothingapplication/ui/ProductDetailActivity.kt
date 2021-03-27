package com.sayyed.onlineclothingapplication.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        binding = DataBindingUtil.setContentView(this@ProductDetailActivity, R.layout.activity_product_detail)

        binding.tvProductDescriptionToggle.setOnClickListener {
           if(binding.tvProductDescription.visibility == View.GONE) {
               binding.tvProductDescription.visibility = View.VISIBLE
           } else {
               binding.tvProductDescription.visibility = View.GONE
           }
        }




    }
}