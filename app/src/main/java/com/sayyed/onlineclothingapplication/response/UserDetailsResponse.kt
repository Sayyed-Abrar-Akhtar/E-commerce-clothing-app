package com.sayyed.onlineclothingapplication.response

import com.sayyed.onlineclothingapplication.models.Users

data class UserDetailsResponse(
    val success: Boolean,
    val users: List<Users>
)