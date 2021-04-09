package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.databinding.ActivitySignUpBinding
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.repository.UserRepository
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignUpBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var userViewModel: UserViewModel


    private var idSharedPref : String? = ""
    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var emailSharedPref : String? = ""
    private  var passwordSharedPref : String? = ""
    private  var tokenSharedPref : String? = ""
    private  var isAdminSharedPref : Boolean = false
    private  var contactSharedPref : String? = ""
    private var isSuccess: Boolean =  false
    private var isLoading: Boolean =  false




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
                "",

        )
        navigationDrawerSetup.addEventListenerToNavItems(this@SignUpActivity, binding.navigationView, isAdminSharedPref)

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

                            }
        }
    }


    private fun userProfileDetail() {

        var userPassword: String = ""
        val passwordInput = binding.etPassword.text.toString()
        val confirmPasswordInput = binding.etConfirmPassword.text.toString()
        if (passwordInput === confirmPasswordInput) {
            userPassword = passwordInput
        } else {
            binding.etConfirmPassword.error = "Passwords did not matched"
            binding.etConfirmPassword.requestFocus()
        }

        val user = User(
                firstName = binding.etFirstName.text.toString(),
                lastName= binding.etLastName.text.toString(),
                //image= binding.etFirstName.text.toString(),
                //contact= binding.et.text.toString(),
                username= binding.etUsername.text.toString(),
                email= binding.etEmail.text.toString(),
                password= userPassword,
        )

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