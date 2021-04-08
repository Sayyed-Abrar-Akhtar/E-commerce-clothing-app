package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.api.ServiceBuilder
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityLoginBinding
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityLoginBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup



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


        /*-----------------------------------LOGIN BTN CLICK LISTENER---------------------------------------------*/
        binding.btnLogin.setOnClickListener {

        }

        /*----------------------------------SIGN UP BTN CLICK LISTENER--------------------------------------------*/
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
    }


    /*-----------------------------------CHECK USER DATA FROM API-------------------------------------------------*/


    /*-----------------------------------CHECK USER DATA FROM API-------------------------------------------------*/
    private fun login() {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        var user: User? = null
        CoroutineScope(Dispatchers.IO).launch {
            user = OnlineClothingDB.getInstance(this@LoginActivity)
                    .userDAO
                    .isUserValid(username, password)
            if (user == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT)
                            .show()
                }
            } else {
                startActivity(Intent(this@LoginActivity,
                        DashboardActivity::class.java))
                saveSharedPref()

            }
        }

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


