package com.sayyed.onlineclothingapplication.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.sayyed.onlineclothingapplication.R
import de.hdodenhof.circleimageview.CircleImageView

class NavigationDrawerSetup : AppCompatActivity() {


    /*-----------------------NAVIGATION DRAWER LAYOUT INITIALIZATION------------------------------*/
    fun navDrawerLayoutInitialization(tvToolbarTitle: TextView, toolbarHeaderText: String) {
        tvToolbarTitle.text = toolbarHeaderText
    }

    /*-----------------------------NAVIGATION DRAWER HEADER SETUP---------------------------------*/
    fun addHeaderText(
            context: Context,
            navigationView: NavigationView,
            name: String,
            contact: String,
            profileImg: String
    ) {

        val view: View = navigationView.getHeaderView(0)
        val tvContact: TextView = view.findViewById(R.id.tvContact)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val imgDrawerLayoutUserProfile: CircleImageView = view.findViewById(R.id.imgDrawerLayoutUserProfile)


        tvName.text = name
        tvContact.text = contact

        Glide.with(context)
                .load(profileImg)
                .into(imgDrawerLayoutUserProfile)

    }

    /*-----------------------NAVIGATION DRAWER ITEM EVENT LISTENERS-------------------------------*/
    fun addEventListenerToNavItems(
            context: Context,
            navigationView: NavigationView,
    ) {

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    val intent = Intent(context, DashboardActivity::class.java)
                    context.startActivity(intent)
                    finish()
                    true
                }
                R.id.account_signUp -> {
                    val intent = Intent(context, SignUpActivity::class.java)
                    context.startActivity(intent)
                    finish()
                    true
                }
                R.id.account_login -> {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    finish()
                    true
                }
                R.id.account_admin -> {
                    val intent = Intent(context, AdminActivity::class.java)
                    context.startActivity(intent)
                    finish()
                    true
                }
                R.id.all_products -> {
                    val intent = Intent(context, ProductActivity::class.java)
                    context.startActivity(intent)
                    finish()
                    true
                }
                R.id.cart -> {
                    val intent = Intent(context, CartActivity::class.java)
                    context.startActivity(intent)
                    finish()
                    true
                }
                R.id.about_us -> {
                    val intent = Intent(context, InformationActivity::class.java)
                    context.startActivity(intent)
                    finish()
                    true
                }
                R.id.locate_us -> {
                    val intent = Intent(context, MapsActivity::class.java)
                    context.startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

}