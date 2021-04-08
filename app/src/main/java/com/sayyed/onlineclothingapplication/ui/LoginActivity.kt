package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.databinding.ActivityLoginBinding
import com.sayyed.onlineclothingapplication.repository.UserRepository
import com.sayyed.onlineclothingapplication.response.UserResponse
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModel
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityLoginBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup

    private lateinit var userViewModel: UserViewModel


    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var contactSharedPref : String? = ""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()

        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
                this@LoginActivity,
                binding.drawer,
                binding.toolbar,
                R.string.open,
                R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*----------------------------------------NAVIGATION DRAWER LAYOUT----------------------------------------*/
        navigationDrawerSetup = NavigationDrawerSetup()
        navigationDrawerSetup.addHeaderText(
            this@LoginActivity,
            binding.navigationView,
            "$firstNameSharedPref $lastNameSharedPref",
            "$contactSharedPref",
            "$imageSharedPref"
        )
        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle,  "Login")
        navigationDrawerSetup.addEventListenerToNavItems(this@LoginActivity, binding.navigationView )

        setupViewModel()




        /*-----------------------------------LOGIN BTN CLICK LISTENER---------------------------------------------*/
        binding.btnLogin.setOnClickListener {
            if(binding.etEmail.text.toString() !== "" && binding.etPassword.text.toString() !== "") {
                authorizedLogin(binding.etEmail.text.toString(), binding.etPassword.text.toString())

                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                intent.putExtra("name", "$firstNameSharedPref $lastNameSharedPref")
                intent.putExtra("image", "$imageSharedPref")
                intent.putExtra("contact", "$contactSharedPref")
                startActivity(intent)

                Toast.makeText(this@LoginActivity, "Logged in Successfully", Toast.LENGTH_LONG)
                    .show()

            } else {
                Toast.makeText(this@LoginActivity, "Please fill the credentials", Toast.LENGTH_LONG)
                    .show()
            }
        }

        /*----------------------------------SIGN UP BTN CLICK LISTENER--------------------------------------------*/
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
    }


    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val repository =  UserRepository()
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
    }

    /*-----------------------------------AUTHORIZED LOGIN---------------------------------------------------------*/
    private fun authorizedLogin(email: String, password: String) {
        userViewModel.authLogin(email, password).observe(this@LoginActivity, {
            it.apiCall()
        })

    }

    /*----------------------------SAVE SHARED PREFERENCES---------------------------------------------------------*/
    private fun saveSharedPref(
        id: String,
        firstName: String,
        lastName: String,
        image: String,
        contact: String,
        email: String,
        token: String,
        isAdmin: Boolean,
        isSuccess: Boolean
    ) {

        val password = binding.etPassword.text.toString()
        val sharedPref = getSharedPreferences("LoginPreference",
                MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("id", id)
        editor.putString("firstName", firstName)
        editor.putString("lastName", lastName)
        editor.putString("image", image)
        editor.putString("contact", contact)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putString("token", token)
        editor.putBoolean("isAdmin", isAdmin)
        editor.putBoolean("isSuccess", isSuccess)

        editor.apply()

    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        val emailSharedPref = sharedPref.getString("email", "")
        val passwordSharedPref = sharedPref.getString("password", "")
        firstNameSharedPref = sharedPref.getString("firstName", "")
        lastNameSharedPref = sharedPref.getString("lastName", "")
        imageSharedPref = sharedPref.getString("image", "")
        contactSharedPref = sharedPref.getString("contact", "")
        if(emailSharedPref !== "" && passwordSharedPref !== "") {
            binding.etEmail.setText(emailSharedPref)
            binding.etPassword.setText(passwordSharedPref)
        }
    }

    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<UserResponse>.apiCall() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    resource.data?.let { data ->
                        val (success, userProfile) = data

                        val password = binding.etPassword.text.toString()
                        val sharedPref = getSharedPreferences("LoginPreference",
                            MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("id", userProfile._id)
                        editor.putString("firstName", userProfile.firstName)
                        editor.putString("lastName", userProfile.lastName)
                        editor.putString("image", userProfile.image)
                        editor.putString("contact", userProfile.contact)
                        editor.putString("email", userProfile.email)
                        editor.putString("password", password)
                        editor.putString("token", userProfile.token)
                        editor.putBoolean("isAdmin", userProfile.isAdmin)
                        editor.putBoolean("isSuccess", success)

                        editor.apply()

                        firstNameSharedPref = userProfile.firstName
                        lastNameSharedPref = userProfile.lastName
                        imageSharedPref = userProfile.image
                        contactSharedPref = userProfile.contact

                        Log.i("USER-TAG", "==>LOADED USER PROFILE FROM API")
                    }
                }
                Status.ERROR -> {
                    println(resource.message)
                }
                Status.LOADING -> {
                    println("Loading to authorised user via /api/users/login/")
                }
            }
        }
    }

}


