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

    fun uploadImage(body: MultipartBody.Part) = liveData {
        emit (Resource.loading(data = null))
        try {
            val image = userRepository.uploadImage(body)
            println("error message=>$image")
            emit(Resource.success(data= image))
        } catch (ex: java.lang.Exception) {
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

    fun allUsers(token: String) = liveData {
        emit( Resource.loading(data = null))
        try {
            val user = userRepository.allUsers(token)
            emit(Resource.success(data = user))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }

    fun deleteUser(token: String, id:String) = liveData {
        emit( Resource.loading(data = null))
        try {
            val user = userRepository.deleteUser(token, id)
            emit(Resource.success(data = user))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }


    fun newAccount(
            firstName: String,
            lastName: String,
            image: String,
            contact: String,
            username: String,
            email: String,
            password: String,
    ) = liveData {
        emit( Resource.loading(data = null))
        try {
            val user = userRepository.newAccount(firstName, lastName, image, contact, username, email, password)
            emit(Resource.success(data = user))
        } catch (ex: Exception) {
            println("error message=>${ex.message}")
            emit(Resource.error(data = null, message = ex.message ?: "Error Occurred!"))
        }
    }

    fun updateUser(
            token: String,
            firstName: String,
            lastName: String,
            contact: String,
            username: String,
            email: String,
            password: String,
            image: String
    ) = liveData {
        emit( Resource.loading(data = null))
        try {
            val user = userRepository.updateUser(token, firstName, lastName, contact, username, email, password, image)
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