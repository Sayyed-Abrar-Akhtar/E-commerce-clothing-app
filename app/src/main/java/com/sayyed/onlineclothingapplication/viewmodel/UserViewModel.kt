package com.sayyed.onlineclothingapplication.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sayyed.onlineclothingapplication.repository.UserRepository
import com.sayyed.onlineclothingapplication.response.UserResponse
import com.sayyed.onlineclothingapplication.utils.Resource

class UserViewModel(private val userRepository: UserRepository): ViewModel(), Observable {


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