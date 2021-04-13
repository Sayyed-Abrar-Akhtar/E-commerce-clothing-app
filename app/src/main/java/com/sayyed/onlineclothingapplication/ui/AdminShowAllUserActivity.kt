package com.sayyed.onlineclothingapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.UserAdminAdapter
import com.sayyed.onlineclothingapplication.databinding.ActivityAdminShowAllUserBinding
import com.sayyed.onlineclothingapplication.eventlistener.OnAdminUserClickListener
import com.sayyed.onlineclothingapplication.models.Users
import com.sayyed.onlineclothingapplication.repository.UserRepository
import com.sayyed.onlineclothingapplication.response.DeleteResponse
import com.sayyed.onlineclothingapplication.response.UserDetailsResponse
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModel
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModelFactory

class AdminShowAllUserActivity : AppCompatActivity(), OnAdminUserClickListener {

    private lateinit var binding: ActivityAdminShowAllUserBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle


    private lateinit var listUser: MutableList<Users>
    private lateinit var adapter: UserAdminAdapter
    private lateinit var userViewModel: UserViewModel

    private lateinit var firstNameSharedPref : String
    private lateinit var lastNameSharedPref : String
    private lateinit var imageSharedPref : String
    private lateinit var contactSharedPref : String
    private  var isAdminSharedPref : Boolean = false
    private  var tokenSharedPref : String? =  ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_show_all_user)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_show_all_user)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/


        firstNameSharedPref = intent.getStringExtra("name").toString()
        imageSharedPref = intent.getStringExtra("image").toString()
        contactSharedPref = intent.getStringExtra("contact").toString()

        getSharedPref()

        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
            this@AdminShowAllUserActivity,
            binding.drawer,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*----------------------------------------NAVIGATION DRAWER LAYOUT----------------------------------------*/
        navigationDrawerSetup = NavigationDrawerSetup()
        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "Admin ")
        navigationDrawerSetup.addHeaderText(
            this@AdminShowAllUserActivity,
            binding.navigationView,
            "$firstNameSharedPref $lastNameSharedPref",
            "$contactSharedPref",
            "$imageSharedPref"
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@AdminShowAllUserActivity, binding.navigationView, isAdminSharedPref)

        setupUI()

        setupViewModel()

        /*----------------------------------------ASK FOR PERMISSIONS---------------------------------------------*/
        setupCategoryObservers()
    }

    override fun onProductDeleteClick(position: Int, userId: String) {
        userViewModel.deleteUser("Bearer $tokenSharedPref", userId).observe(this@AdminShowAllUserActivity, {
            it.loadApiToDelData()
        })
        val handler = Handler(Looper.getMainLooper())
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewUserAdmin.visibility = View.GONE
        handler.postDelayed({
            setupCategoryObservers()
            binding.progressBar.visibility = View.GONE
            binding.recyclerViewUserAdmin.visibility = View.VISIBLE
        }, 1000)
    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        firstNameSharedPref = sharedPref.getString("firstName", "").toString()
        lastNameSharedPref = sharedPref.getString("lastName", "").toString()
        imageSharedPref = sharedPref.getString("image", "").toString()
        contactSharedPref = sharedPref.getString("contact", "").toString()
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
        tokenSharedPref = sharedPref.getString("token", "")
    }

    /*---------------------------------------------SET UP UI------------------------------------------------------*/
    private fun setupUI() {
        binding.recyclerViewUserAdmin.layoutManager = LinearLayoutManager(this@AdminShowAllUserActivity)
        listUser = mutableListOf()
        adapter = UserAdminAdapter(this, listUser, this)
        binding.recyclerViewUserAdmin.adapter = adapter
    }

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val repository =  UserRepository()
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
    }


    /*-------------------------------------SET DATA FROM API TO DISPLAY-------------------------------------------*/
    private fun setupCategoryObservers() {
        userViewModel.allUsers("Bearer $tokenSharedPref").observe(this, {
            it.loadApiData()
        })
    }

    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<UserDetailsResponse>.loadApiData() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewUserAdmin.visibility = View.VISIBLE

                    resource.data?.let { user ->
                        listUser.clear()
                        listUser.addAll(user.users)
                        adapter.notifyDataSetChanged()
                        Log.i("CategoryTAG", "==>LOADED CATEGORY DATA FROM API")
                    }
                }
                Status.ERROR -> {
                    binding.recyclerViewUserAdmin.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    println("=========================ERROR====================")
                    println(resource.data)
                    println(resource.message)
                    println("==================================================")
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewUserAdmin.visibility = View.GONE
                    println("=========================LOADER====================")
                    println("!!! LOADING... !!!")
                    println("===================================================")
                }

            }
        }
    }

    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<DeleteResponse>.loadApiToDelData() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewUserAdmin.visibility = View.VISIBLE

                    resource.data?.let { data ->
                        Toast.makeText(this@AdminShowAllUserActivity, data.message, Toast.LENGTH_SHORT).show()
                        Log.i("productDeleteTag", "==>PRODUCT DELETED FROM API")
                    }
                }

                Status.ERROR -> {
                    binding.recyclerViewUserAdmin.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    println("=========================ERROR====================")
                    println(resource.data)
                    println(resource.message)
                    println("==================================================")
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewUserAdmin.visibility = View.GONE
                    println("=========================LOADER====================")
                    println("!!! LOADING... !!!")
                    println("===================================================")
                }
            }
        }
    }


}