package com.sayyed.onlineclothingapplication.eventlistener

import com.sayyed.onlineclothingapplication.models.Product

interface OnAdminProductClickListener {
    fun OnProductEditClick(position: Int, product: Product)
    fun OnProductDeleteClick(position: Int, productId: String)
}