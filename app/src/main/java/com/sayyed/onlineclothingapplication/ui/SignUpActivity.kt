package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.database.UserDB
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {

    private lateinit var etFirstName : EditText
    private lateinit var etLastName : EditText
    private lateinit var etEmail : EditText
    private lateinit var etUsername : EditText
    private lateinit var etPassword : EditText
    private lateinit var etConfirmPassword : EditText
    private lateinit var btnSignUp : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)


        btnSignUp.setOnClickListener {

            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val email = etEmail.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()


            if (password != confirmPassword) {
                etConfirmPassword.error = "Password did not matched!!"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            } else {
                /*
                val user = User(firstName, lastName, email, username, password)

                CoroutineScope(Dispatchers.IO).launch {
                    UserDB
                        .getInstance(this@SignUpActivity)
                        .getUserDao()
                        .insertUser(user)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SignUpActivity, "User data inserted Successfully!!", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this@SignUpActivity,
                                LoginActivity::class.java))

                        clearFields()
                    }
                }

                */
                val user = User(firstName = firstName, lastName = lastName, contact = "02312", email= email, username= username, password=password, role = "admin")

                // Api code goes here
                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        val repository = UserRepository()
                        val response = repository.registerUser(user)

                        if (response.success == true) {

                            /*
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@SignUpActivity, "User registered", Toast.LENGTH_LONG).show()
                            }

                            */

                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@SignUpActivity, "User data inserted Successfully!!", Toast.LENGTH_SHORT).show()

                                startActivity(Intent(this@SignUpActivity,
                                        LoginActivity::class.java))

                                clearFields()
                            }
                        }

                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignUpActivity, " error occured: ${ex.toString()}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }


    }

    private fun clearFields() {
        etFirstName.text.clear()
        etLastName.text.clear()
        etEmail.text.clear()
        etUsername.text.clear()
        etPassword.text.clear()
        etConfirmPassword.text.clear()

    }
}