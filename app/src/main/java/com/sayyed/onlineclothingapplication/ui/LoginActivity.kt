package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityLoginBinding
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.repository.UserRepository
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModel
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModelFactory
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModel
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityLoginBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup

    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //=======================================================//
        /*--------------------------------------------------------
        TODO create view model and implement data binding pending
         */


        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)


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
            "Login",
            "",
            ""
        )
        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle,  "Login")
        navigationDrawerSetup.addEventListenerToNavItems(this@LoginActivity, binding.navigationView )

        /*----------------------------------------ASK FOR PERMISSIONS---------------------------------------------*/
        if (!hasPermission()) {
            requestPermission()
        }

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()


        setupViewModel()
        /*-----------------------------------LOGIN BTN CLICK LISTENER---------------------------------------------*/
        binding.btnLogin.setOnClickListener {
            authorizedLogin(binding.etUsername.text.toString(), binding.etPassword.text.toString())
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

    /*-----------------------------------CHECK USER DATA FROM API-------------------------------------------------*/


    /*-----------------------------------AUTHORIZED LOGIN---------------------------------------------------------*/
    private fun authorizedLogin(email: String, password: String) {
        userViewModel.authLogin(email, password).observe(this@LoginActivity, {
            it?.let { user ->
                println("&^*^&%^%%&^^%^&%%%$%^%^%&^")
                println(user.data)
                println("&^*^&%^%%&^^%^&%%%$%^%^%&^")
            }
        })
    }

    /*----------shared preferences-------------- */
    private fun saveSharedPref() {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val sharedPref = getSharedPreferences("LoginPreference",
                MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()

    }

    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        val password = sharedPref.getString("password", "")
        if(username != null) {
            binding.etUsername.setText(username)
            binding.etPassword.setText(password)
        }
    }

    /*----------end shared preferences-------------- */




    /*----------Permissions-------------- */
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
                this@LoginActivity,
                permissions, 234
        )
    }
    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                            this,
                            permission
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
            }
        }
        return hasPermission
    }

    private val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    /*----------end permissions-------------- */


}


