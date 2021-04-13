package com.sayyed.onlineclothingapplication.eventlistener

import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.models.Product

interface OnAdminCategoryClickListener {
    fun onProductEditClick(position: Int, category: Category)
    fun onProductDeleteClick(position: Int, categoryId: String)
}