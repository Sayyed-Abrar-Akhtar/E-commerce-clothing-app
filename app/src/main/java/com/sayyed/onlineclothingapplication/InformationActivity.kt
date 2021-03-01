package com.sayyed.onlineclothingapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class InformationActivity : AppCompatActivity() {

    private lateinit var btnLocateUs: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        btnLocateUs = findViewById(R.id.btnLocateUs)

        btnLocateUs.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}