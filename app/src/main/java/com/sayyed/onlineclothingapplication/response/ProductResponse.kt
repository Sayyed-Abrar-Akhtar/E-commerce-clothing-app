package com.sayyed.onlineclothingapplication.response

import com.sayyed.onlineclothingapplication.models.Product

class ProductResponse (
    val Product: List<Product>,
    val success: Boolean
)