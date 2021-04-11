package com.sayyed.onlineclothingapplication.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sayyed.onlineclothingapplication.repository.UserRepository
import com.sayyed.onlineclothingapplication.response.UserResponse
import com.sayyed.onlineclothingapplication.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(private val userRepository: UserRepository): ViewModel(), Observable {




    fun newAccount(
            firstName: RequestBody,
            lastName: RequestBody,
            contact: RequestBody,
            username: RequestBody,
            email: RequestBody,
            password: RequestBody,
            body: MultipartBody.Part
    ) = liveData {
        emit( Resource.loading(data = null))
        try {
            val user = userRepository.newAccount(firstName, lastName, contact, username, email, password, body)
            emit(Resource.success(data = user))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }

    fun authLogin(email: String, password:String) = liveData {
        emit( Resource.loading(data = null))
        try {
             val user = userRepository.authLogin(email, password)
            emit(Resource.success(data = user))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}