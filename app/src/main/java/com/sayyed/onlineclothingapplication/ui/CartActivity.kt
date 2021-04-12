package com.sayyed.onlineclothingapplication.ui

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.databinding.ActivityCartBinding
import com.sayyed.onlineclothingapplication.databinding.ActivitySignUpBinding
import com.sayyed.onlineclothingapplication.notification.NotificationChannels
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModel

class CartActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCartBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle

    private var headerText: String = "Your Cart"



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


    private var product_title: String = ""
    private var product_image: String = ""
    private var product_price: String = ""
    private var product_qty: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        binding = DataBindingUtil.setContentView(this@CartActivity, R.layout.activity_cart)


        val product_title = intent.getStringExtra("product_title")
        val product_image = intent.getStringExtra("product_image")
        val product_price = intent.getStringExtra("product_price")
        val product_qty = intent.getStringExtra("product_qty")


        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()

        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
            this@CartActivity,
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
            this@CartActivity,
            binding.navigationView,
            "$firstNameSharedPref $lastNameSharedPref",
            "$contactSharedPref",
            "$imageSharedPref",

            )
        navigationDrawerSetup.addEventListenerToNavItems(this@CartActivity, binding.navigationView, isAdminSharedPref)


        setUpUI("$product_image", "$product_title", "$product_qty", "$product_price")


        binding.btnContinuePayment.setOnClickListener {
            showNotification()
            val intent = Intent(this@CartActivity, ShippingActivity::class.java)
            intent.putExtra("product_title", product_title)
            startActivity(intent)
            finish()
        }
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

    /*----------------------------SET UP UI-----------------------------------------------------------------------*/
    private fun setUpUI(image: String, title: String, qty: String, price: String) {
        Glide.with(this@CartActivity)
            .load(image).into(binding.imgItem)

        binding.tvProductTitle.text = title
        binding.tvQtySelected.text = qty
        binding.tvShowQtyPrice.text = "$qty * $price = ${(qty.toFloat()*price.toFloat())}"

        println("$idSharedPref")
    }

    /*----------------------------SHOW ORDER NOTIFICATION-------------------------------------------------------*/
    private fun showNotification() {
        val notificationManager = NotificationManagerCompat.from(this)
        val activityIntent = Intent(this, MapsActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)

        val notificationChannels = NotificationChannels(this)
        notificationChannels.createNotificationChannels()

        val notification = NotificationCompat.Builder(this, notificationChannels.CHANNEL_LOW)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("ORDER CREATED")
            .setContentText("$product_qty of$product_title order created")
            .setColor(Color.BLUE)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}