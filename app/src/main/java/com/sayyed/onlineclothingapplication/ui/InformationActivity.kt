package com.sayyed.onlineclothingapplication.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.databinding.ActivityInformationBinding
import de.hdodenhof.circleimageview.CircleImageView

class InformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformationBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_information)

        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
                this@InformationActivity,
                binding.drawer,
                binding.toolbar,
                R.string.open,
                R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*----------------------------------------NAVIGATION DRAWER LAYOUT----------------------------------------*/
        navigationDrawerSetup = NavigationDrawerSetup()
        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "About Us")
        navigationDrawerSetup.addHeaderText(
                this@InformationActivity,
                binding.navigationView,
                "",
                "",
                "https://i.pinimg.com/280x280_RS/45/57/31/455731391ed7c0b084f935d32a0f2612.jpg")
        navigationDrawerSetup.addEventListenerToNavItems(this@InformationActivity, binding.navigationView)

    }


}