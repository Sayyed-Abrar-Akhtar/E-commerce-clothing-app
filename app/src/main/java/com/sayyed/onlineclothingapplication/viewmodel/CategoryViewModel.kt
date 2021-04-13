package com.sayyed.onlineclothingapplication.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class CategoryViewModel(private val categoryRepository: CategoryRepository): ViewModel(), Observable {

    fun uploadImage(body: MultipartBody.Part) = liveData(Dispatchers.IO) {
        emit (Resource.loading(data = null))
        try {
            val image = categoryRepository.uploadImage(body)
            println("error message=>$image")
            emit(Resource.success(data= image))
        } catch (ex: java.lang.Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }

    fun createCategory(token: String, name: String, image: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = categoryRepository.createCategory(token, name, image)))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!" ))
        }
    }

    fun updateCategory(token: String, id: String, name: String, image: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = categoryRepository.updateCategory(token, id, name, image)))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!" ))
        }
    }

    fun getCategory() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = categoryRepository.getCategory()))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!" ))
        }
    }

    fun getCategoryName() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = categoryRepository.getCategoryName()))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!" ))
        }
    }

    fun getCategoryId(name: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = categoryRepository.getCategoryId(name)))
        } catch (ex: Exception) {
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!" ))
        }
    }

    fun deleteCategory(token: String, id: String) = liveData {
        emit(Resource.loading(data = null))
        try {
            val data = categoryRepository.deleteCategory(token, id)
            emit(Resource.success(data = data))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
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

    fun deleteCategoryFromRoom(id: String) = viewModelScope.launch {
        try {
            categoryRepository.deleteCategoryFromRoom(id)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    val categoryFromRoom = categoryRepository.retrieveCategoryFromRoom

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}