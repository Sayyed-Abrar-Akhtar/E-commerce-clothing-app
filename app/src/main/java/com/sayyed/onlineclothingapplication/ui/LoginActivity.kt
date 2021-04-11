package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
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
import com.sayyed.onlineclothingapplication.utils.Network

class LoginActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityLoginBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup

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
        setContentView(R.layout.activity_login)


        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)

        val action = intent.getStringExtra("logout")

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
        navigationDrawerSetup.addEventListenerToNavItems(this@LoginActivity, binding.navigationView, isAdminSharedPref )

        setupViewModel()

        /*-----------------------------------LOGIN BTN CLICK LISTENER---------------------------------------------*/
        binding.btnLogin.setOnClickListener {
            if(checkNetwork()) {
                ifFieldEmpty()
                authorizedLogin(binding.etEmail.text.toString(), binding.etPassword.text.toString())

                binding.btnLogin.text = getString(R.string.loading)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    if (isSuccess) {
                        Toast.makeText(this@LoginActivity, "Logged in successfully", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        binding.btnLogin.text = getString(R.string.login)
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_LONG).show()
                    }
                }, 1500)
            } else {
                Toast.makeText(this@LoginActivity, getString(R.string.no_internet), Toast.LENGTH_LONG).show()
            }

        }


        /*----------------------------------SIGN UP BTN CLICK LISTENER--------------------------------------------*/
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
    }

    /*--------------------------------------------CHECK NETWORK---------------------------------------------------*/
    private fun checkNetwork(): Boolean{
        return Network.isNetworkAvailable(this@LoginActivity)
    }


    /*--------------------------------------------CHECK EMPTY FIELD-----------------------------------------------*/
    private fun ifFieldEmpty() {
        if (binding.etEmail.text.toString() == "") {
            binding.etEmail.error = "Please enter email address"
            binding.etEmail.requestFocus()
            return
        }
        if (binding.etPassword.text.toString() == "") {
            binding.etPassword.error = "Please enter password"
            binding.etPassword.requestFocus()
            return
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
    private fun saveSharedPref() {
        val password = binding.etPassword.text.toString()
        val sharedPref = getSharedPreferences("LoginPreference",
                MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("id", idSharedPref)
        editor.putString("firstName", firstNameSharedPref)
        editor.putString("lastName", lastNameSharedPref)
        editor.putString("image", imageSharedPref)
        editor.putString("contact", contactSharedPref)
        editor.putString("email", emailSharedPref)
        editor.putString("password", password)
        editor.putString("token", tokenSharedPref)
        editor.putBoolean("isAdmin", isAdminSharedPref)
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
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
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

                        if(success && userProfile.token !== "") {
                            idSharedPref =userProfile._id
                            firstNameSharedPref = userProfile.firstName
                            lastNameSharedPref = userProfile.lastName
                            imageSharedPref = userProfile.image
                            contactSharedPref = userProfile.contact
                            emailSharedPref = userProfile.email
                            passwordSharedPref = binding.etPassword.text.toString()
                            tokenSharedPref = userProfile.token
                            isAdminSharedPref = userProfile.isAdmin
                            isSuccess = success
                            isLoading = false
                            saveSharedPref()
                        }


                        Log.i("USER-TAG", "==>LOADED USER PROFILE FROM API")
                    }
                }
                Status.ERROR -> {
                    isLoading = false
                }
                Status.LOADING -> {
                    isLoading = true
                }
            }
        }
    }

}


