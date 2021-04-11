package com.sayyed.onlineclothingapplication.ui

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.databinding.ActivityCartBinding
import com.sayyed.onlineclothingapplication.databinding.ActivityShippingBinding
import com.sayyed.onlineclothingapplication.notification.NotificationChannels

class ShippingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShippingBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle

    private var headerText: String = "Shipping details"



    private var idSharedPref : String? = ""
    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var emailSharedPref : String? = ""
    private  var usernameSharedPref : String? = ""
    private  var passwordSharedPref : String? = ""
    private  var tokenSharedPref : String? = ""
    private  var isAdminSharedPref : Boolean = false
    private  var contactSharedPref : String? = ""
    private var productName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping)

        binding = DataBindingUtil.setContentView(this@ShippingActivity, R.layout.activity_shipping)


        productName = intent.getStringExtra("product_title").toString()

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()

        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
            this@ShippingActivity,
            binding.drawer,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*----------------------------------------NAVIGATION DRAWER LAYOUT----------------------------------------*/
        navigationDrawerSetup = NavigationDrawerSetup()
        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, headerText)
        navigationDrawerSetup.addHeaderText(
            this@ShippingActivity,
            binding.navigationView,
            "$firstNameSharedPref $lastNameSharedPref",
            "$contactSharedPref",
            "$imageSharedPref",

            )
        navigationDrawerSetup.addEventListenerToNavItems(this@ShippingActivity, binding.navigationView, isAdminSharedPref)


        binding.btnOrder.setOnClickListener {
            clearField()
            Toast.makeText(this@ShippingActivity, "Order received", Toast.LENGTH_SHORT).show()
            binding.tvOrderMsg.visibility = View.VISIBLE
            binding.btnLocateUs.visibility = View.VISIBLE
            showNotification()
        }

        binding.btnLocateUs.setOnClickListener {
            val intent = Intent(this@ShippingActivity, MapsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /*----------------------------CLEAR FIELDS-------------------------------------------------------*/
    private fun clearField() {
        binding.etAddress.text.clear()
        binding.etCity.text.clear()
        binding.etCountry.text.clear()
        binding.etPostalCode.text.clear()
        binding.etMobNum.text.clear()
    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        idSharedPref = sharedPref.getString("_id", "")
        emailSharedPref = sharedPref.getString("email", "")
        usernameSharedPref = sharedPref.getString("username", "")
        passwordSharedPref = sharedPref.getString("password", "")
        firstNameSharedPref = sharedPref.getString("firstName", "")
        lastNameSharedPref = sharedPref.getString("lastName", "")
        imageSharedPref = sharedPref.getString("image", "")
        contactSharedPref = sharedPref.getString("contact", "")
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
        tokenSharedPref = sharedPref.getString("token", "")

    }

    /*----------------------------SHOW ORDER NOTIFICATION-------------------------------------------------------*/
    private fun showNotification() {
        val notificationManager = NotificationManagerCompat.from(this)
        val activityIntent = Intent(this, MapsActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)

        val notificationChannels = NotificationChannels(this)
        notificationChannels.createNotificationChannels()

        val notification = NotificationCompat.Builder(this, notificationChannels.CHANNEL_HIGH)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("ORDER CONFIRMED")
            .setContentText("The order for $productName is confirmed and the delivery at ${binding.etAddress.text} is on the way!")
            .setColor(Color.BLUE)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}