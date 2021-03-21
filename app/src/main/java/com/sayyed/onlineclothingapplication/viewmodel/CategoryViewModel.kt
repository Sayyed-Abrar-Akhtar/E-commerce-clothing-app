package com.sayyed.onlineclothingapplication.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CategoryViewModel(private val categoryRepository: CategoryRepository): ViewModel(), Observable {

    fun getCategory() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = categoryRepository.getCategory()))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!" ))
        }
    }

    fun insertCategoryIntoRoom() = viewModelScope.launch {
        try {
            val categoriesLive = categoryRepository.getCategory().category
            for (category in categoriesLive) {
                categoryRepository.insertCategoryToRoom(category)
            }
        } catch(ex: Exception) {
            ex.printStackTrace()
        }
    }

    val categoryFromRoom = categoryRepository.retrieveCategoryFromRoom

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}