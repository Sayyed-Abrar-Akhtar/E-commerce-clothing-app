package com.sayyed.onlineclothingwear

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.support.wearable.activity.WearableActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.sayyed.onlineclothinglibrary.wearrepository.UserWearRepository
import java.lang.Exception

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : WearableActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Enables Always-on
        setAmbientEnabled()
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener{
            if(validate()){
                login()
            }
        }
    }

    private fun validate() :Boolean{
        var flag = true
        if(TextUtils.isEmpty(etEmail.text)){
            etEmail.error = "Enter email"
            etEmail.requestFocus()
            flag = false
        }else if(TextUtils.isEmpty(etPassword.text)){
            etPassword.error = "Enter password"
            etPassword.requestFocus()
            flag = false
        }
        return flag
    }

    private fun login() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserWearRepository()
                val response = repository.authLogin(email, password)
                if (response.success) {
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Looper.prepare();
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    Looper.loop()

                }
            } catch (ex: Exception) {
                    Log.d("Error",ex.toString())
                    Looper.prepare()
                    Toast.makeText(
                        this@LoginActivity,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Looper.loop()
            }
        }
    }

}