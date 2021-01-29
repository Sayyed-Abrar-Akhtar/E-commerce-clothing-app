package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ViewPagerAdapter
import com.sayyed.onlineclothingapplication.database.UserDB
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.fragments.LoginFragment
import com.sayyed.onlineclothingapplication.fragments.SignUpFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnLogin : Button
    private lateinit var btnSignUp : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*----------Permissions-------------- */
        if (!hasPermission()) {
            requestPermission()
        }

        /*----------End Permissions-------------- */

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        getSharedPref()


        btnLogin.setOnClickListener {
            login()
        }

        btnSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }


    }

    private fun login() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        var user: User? = null
        CoroutineScope(Dispatchers.IO).launch {
            user = UserDB.getInstance(this@LoginActivity)
                    .getUserDao()
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
                clearInputFields()
            }
        }

    }

    /*----------shared preferences-------------- */
    private fun saveSharedPref() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
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
            etUsername.setText(username)
            etPassword.setText(password)
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


    private fun clearInputFields() {
        etUsername.text.clear()
        etPassword.text.clear()
    }

}


