package com.sayyed.onlineclothingapplication

import com.sayyed.onlineclothingapplication.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UserTest {

    private lateinit var userRepository: UserRepository

    @Test
    fun checkUserLogin () = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.authLogin("akhtars10@uni.coventry.ac.uk", "sayyed-abrar")
        val expectedResult = true
        val actualResult = response.success

        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkUserRegister () = runBlocking {
        userRepository = UserRepository()

        val response =userRepository.newAccount(
            "Rehman",
            "Khan",
            "/uploads/no-image.png",
            "980000000000",
            "rehman",
            "rehman@khan.com",
            "rehman"
        )

        val expectedResult = true
        val actualResult = response.success

        Assert.assertEquals(expectedResult, actualResult)

    }
}