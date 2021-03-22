package com.sayyed.onlineclothingapplication.response

import com.sayyed.onlineclothingapplication.models.Product

class ProductResponse (
    val Product: ArrayList<Product>,
    val success: Boolean
)