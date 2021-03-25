package com.sayyed.onlineclothingapplication.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.database.UserDB
import com.sayyed.onlineclothingapplication.databinding.ActivitySignUpBinding
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.repository.UserRepository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignUpBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)

        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
                this@SignUpActivity,
                binding.drawer,
                binding.toolbar,
                R.string.open,
                R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*----------------------------------------NAVIGATION DRAWER LAYOUT----------------------------------------*/
        navigationDrawerSetup = NavigationDrawerSetup()
        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "Create New Account")
        navigationDrawerSetup.addHeaderText(
                this@SignUpActivity,
                binding.navigationView,
                "",
                "",
                ""
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@SignUpActivity, binding.navigationView)

        /*-----------------------------------SIGN UP BUTTON CLICK LISTENER----------------------------------------*/
        binding.btnSignUp.setOnClickListener {

            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val email = binding.etEmail.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Password did not matched!!"
                binding.etConfirmPassword.requestFocus()
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
                            Toast.makeText(this@SignUpActivity, " error occured: $ex", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    /*----------------------------------CLEAR VIEWS---------------------------------------------------------------*/
    private fun clearFields() {
        binding.etFirstName.text.clear()
        binding.etLastName.text.clear()
        binding.etEmail.text.clear()
        binding.etUsername.text.clear()
        binding.etPassword.text.clear()
        binding.etConfirmPassword.text.clear()

    }
}