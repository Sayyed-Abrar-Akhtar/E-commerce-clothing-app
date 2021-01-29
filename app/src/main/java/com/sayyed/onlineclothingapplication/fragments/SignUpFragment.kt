package com.sayyed.onlineclothingapplication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.database.UserDB
import com.sayyed.onlineclothingapplication.entities.User
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class SignUpFragment : Fragment() {
    private lateinit var etFirstName : EditText
    private lateinit var etLastName : EditText
    private lateinit var etEmail : EditText
    private lateinit var etUsername : EditText
    private lateinit var etPassword : EditText
    private lateinit var etConfirmPassword : EditText
    private lateinit var btnSignUp : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        etFirstName = view.findViewById(R.id.etFirstName)
        etLastName = view.findViewById(R.id.etLastName)
        etEmail = view.findViewById(R.id.etEmail)
        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        btnSignUp = view.findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val email = etEmail.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val context = activity as AppCompatActivity

            if (password != confirmPassword) {
                etConfirmPassword.error = "Password did not matched!!"
                etConfirmPassword.requestFocus()

            } else {
                val user = User(firstName, lastName, email, username, password)

                CoroutineScope(Dispatchers.IO).launch {
                    UserDB
                            .getInstance(context)
                            .getUserDao()
                            .insertUser(user)

                    withContext(Main) {
                        Toast.makeText(context, "User data inserted Successfully!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        return view
    }


}