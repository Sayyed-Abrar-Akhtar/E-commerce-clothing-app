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
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var productRepository: ProductRepository

    private lateinit var categoryDAO: CategoryDAO


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


}