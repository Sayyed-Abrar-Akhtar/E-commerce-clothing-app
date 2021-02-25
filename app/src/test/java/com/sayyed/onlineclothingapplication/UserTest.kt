package com.sayyed.onlineclothingapplication

import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UserTest {

    private lateinit var userRepository: UserRepository

    @Test
    fun checkUserLogin () = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.checkUser("sayyed", "sayyed123")
        val expectedResult = true
        val actualResult = response.success

        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkUserRegister () = runBlocking {
        userRepository = UserRepository()

        val response =userRepository.registerUser(User("jkj", "sayyed", "Akhtar", "sayy@gmail.com", "sayyed.abrar", "sayyed") )

        val expectedResult = true
        val actualResult = response.success

        Assert.assertEquals(expectedResult, actualResult)

    }
}