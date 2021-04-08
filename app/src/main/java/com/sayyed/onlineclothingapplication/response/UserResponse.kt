package com.sayyed.onlineclothingapplication.response

import com.sayyed.onlineclothingapplication.models.UserLoggedIn

data class UserResponse (
        val success: Boolean,
        val userLoggedIn: UserLoggedIn
)