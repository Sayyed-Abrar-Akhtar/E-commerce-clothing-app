package com.sayyed.onlineclothingapplication.eventlistener

import com.sayyed.onlineclothingapplication.models.Product

interface OnAdminProductClickListener {
    fun onProductEditClick(position: Int, product: Product)
    fun onProductDeleteClick(position: Int, productId: String)
}