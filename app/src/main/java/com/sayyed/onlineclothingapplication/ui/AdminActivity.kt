package com.sayyed.onlineclothingapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.databinding.ActivityAdminBinding
import com.sayyed.onlineclothingapplication.databinding.ActivityProductBinding
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModel
import java.util.*

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle

    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var contactSharedPref : String? = ""
    private  var isAdminSharedPref : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)


        binding = DataBindingUtil.setContentView(this@AdminActivity, R.layout.activity_admin)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()


        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
                this,
                binding.drawer,
                binding.toolbar,
                R.string.open,
                R.string.close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*----------------------------------------NAVIGATION DRAWER LAYOUT----------------------------------------*/
        navigationDrawerSetup = NavigationDrawerSetup()

        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "Admin Dashboard")

        navigationDrawerSetup.addHeaderText(
                this@AdminActivity,
                binding.navigationView,
                "$firstNameSharedPref $lastNameSharedPref",
                "$contactSharedPref",
                "$imageSharedPref"
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@AdminActivity, binding.navigationView, isAdminSharedPref)

       binding.btnAddProduct.setOnClickListener{
           val intent = Intent(this@AdminActivity, ProductCreateUpdateActivity::class.java)
           intent.putExtra("header", "Add Product")
           startActivity(intent)
           finish()
       }

    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        firstNameSharedPref = sharedPref.getString("firstName", "")
        lastNameSharedPref = sharedPref.getString("lastName", "")
        imageSharedPref = sharedPref.getString("image", "")
        contactSharedPref = sharedPref.getString("contact", "")
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
    }

}