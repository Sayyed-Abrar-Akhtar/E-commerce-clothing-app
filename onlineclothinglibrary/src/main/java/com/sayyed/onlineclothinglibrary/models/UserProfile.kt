package com.sayyed.onlineclothingapplication.models

data class UserProfile (
        val _id: String,
        val contact: String,
        val email: String,
        val firstName: String,
        val image: String,
        val isAdmin: Boolean,
        val lastName: String,
        val token: String,
        val username: String
)