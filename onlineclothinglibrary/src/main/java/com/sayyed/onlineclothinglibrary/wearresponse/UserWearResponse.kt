package com.sayyed.onlineclothingapplication.response

import com.sayyed.onlineclothingapplication.models.UserProfile

data class UserWearResponse (
        val success: Boolean,
        val userProfile: UserProfile
)