package com.sayyed.onlineclothingapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sayyed.onlineclothingapplication.ui.DashboardActivity
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.database.UserDB
import com.sayyed.onlineclothingapplication.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var etUsername : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnLogin : Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {


            loginCustomer()

        }
        return view
    }

    private fun loginCustomer() {
        val username: String = etUsername.text.toString()
        val password: String = etPassword.text.toString()

        val context = activity as AppCompatActivity
        var user : User? = null
        CoroutineScope(Dispatchers.IO).launch {
            user = UserDB.getInstance(context).getUserDao().isUserValid(username, password)
            
            if (user != null) {
                val intent = Intent(context, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                println("$user")
                
                
            }
        }


        
    }




}