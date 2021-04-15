package com.sayyed.onlineclothingapplication

import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.manipulation.Ordering


class UserTest {

    private lateinit var userRepository: UserRepository


    private  val token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwNzU1MDI1NGQxNjQ1M2Y0MDQxNzQyZCIsImlhdCI6MTYxODQ4MTU0MCwiZXhwIjoxNjIxMDczNTQwfQ.Wag7vYxyhbTEorIMePpOqQCCNP9IWXGPY5Tqptna5wc"

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

        val response = userRepository.newAccount(
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

    @Test
    fun checkUpdateUser() = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.updateUser(
            "Bearer $token",
            "Sayyed Abrar",
            "Akhtar",
            "123456789",
            "sayyed",
            "",
            "sayyed",
            "https://cdn.pixabay.com/photo/2018/08/28/13/29/avatar-3637561_960_720.png"
        )

        val expectedResult = true
        val actualResult = response.success

        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkRetrieveAllUsers() = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.allUsers(
            "Bearer $token"
        )

        val expectedResult = true
        val actualResult = response.success

        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkDeleteUser() = runBlocking {

        userRepository = UserRepository()
        val response = userRepository.deleteUser(
            "Bearer $token",
            "607881e15e83e027f850cf74"
        )
        val expectedResult = true
        val actualResult = response.success

        Assert.assertEquals(expectedResult, actualResult)
    }

}