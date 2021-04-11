package com.sayyed.onlineclothingapplication.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.databinding.ActivitySignUpBinding
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.models.UserProfile
import com.sayyed.onlineclothingapplication.repository.UserRepository
import com.sayyed.onlineclothingapplication.response.UserResponse
import com.sayyed.onlineclothingapplication.utils.FileUpload
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModel
import com.sayyed.onlineclothingapplication.viewmodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignUpBinding
    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var userViewModel: UserViewModel


    private var idSharedPref : String? = ""
    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var emailSharedPref : String? = ""
    private  var passwordSharedPref : String? = ""
    private  var tokenSharedPref : String? = ""
    private  var isAdminSharedPref : Boolean = false
    private  var contactSharedPref : String? = ""
    private var isSuccess: Boolean =  false
    private var isLoading: Boolean =  false

    private var REQUEST_GALLERY_CODE = 1
    private var REQUEST_CAMERA_CODE = 0
    private var imageUrl: String? = null

    private lateinit var firstNamePart: RequestBody
    private lateinit var lastNamePart: RequestBody
    private lateinit var contactPart: RequestBody
    private lateinit var usernamePart: RequestBody
    private lateinit var emailPart: RequestBody
    private lateinit var passwordPart: RequestBody


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()

        /*---------------------------------------HAMBURGER MENU BAR TOGGLE----------------------------------------*/
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
                this@SignUpActivity,
                binding.drawer,
                binding.toolbar,
                R.string.open,
                R.string.close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*----------------------------------------NAVIGATION DRAWER LAYOUT----------------------------------------*/
        navigationDrawerSetup = NavigationDrawerSetup()
        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "Create New Account")
        navigationDrawerSetup.addHeaderText(
                this@SignUpActivity,
                binding.navigationView,
                "$firstNameSharedPref $lastNameSharedPref",
                "$contactSharedPref",
                "$imageSharedPref",

                )
        navigationDrawerSetup.addEventListenerToNavItems(this@SignUpActivity, binding.navigationView, isAdminSharedPref)

        setupViewModel()

        /*-----------------------------------IMAGE VIEW CLICK LISTENER--------------------------------------------*/
        binding.imgUserProfile.setOnClickListener {
            FileUpload.loadPopUpMenu(this@SignUpActivity, this@SignUpActivity, binding.imgUserProfile)
        }
        /*-----------------------------------SIGN UP BUTTON CLICK LISTENER----------------------------------------*/
        binding.btnSignUp.setOnClickListener {
            userProfileDetail()
            val body = FileUpload.setMimeType(imageUrl)
            userViewModel.newAccount(
                    firstNamePart,
                    lastNamePart,
                    contactPart,
                    usernamePart,
                    emailPart,
                    passwordPart,
                    body
            ).observe(this@SignUpActivity, {
                it.apiCall()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    if(isSuccess){
                        Toast.makeText(this@SignUpActivity, "New user created successfully", Toast.LENGTH_SHORT).show()
                        val intent  = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }, 900)
            })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_GALLERY_CODE) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                binding.imgUserProfile.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = FileUpload.bitmapToFile(imageBitmap, getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        .toString(),"$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                binding.imgUserProfile.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }
    }


    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val repository =  UserRepository()
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
    }

    /*--------------------------------------VIEW BINDING VIEW MODEL-----------------------------------------------*/
    private fun userProfileDetail() {

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val contact = binding.etContact.text.toString()
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if(password != confirmPassword) {
            binding.etConfirmPassword.error ="Password did not matched!!"
            binding.etConfirmPassword.requestFocus()
            return
        }
        firstNamePart = firstName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        lastNamePart = lastName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        contactPart = contact.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        usernamePart = username.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        emailPart = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        passwordPart = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }


    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<UserResponse>.apiCall() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    resource.data?.let { data ->
                        val (success, userProfile) = data

                        if(success && userProfile.token !== "") {
                            saveSharedPref(userProfile, success)
                            isLoading = false
                            isSuccess = success
                        }
                        clearFields()
                        Log.i("USER-TAG", "==>LOADED USER PROFILE FROM API ==> $data")
                    }
                }
                Status.ERROR -> {
                    isLoading = false
                }
                Status.LOADING -> {
                    isLoading = true
                }
            }
        }
    }

    /*----------------------------SAVE SHARED PREFERENCES---------------------------------------------------------*/
    private fun saveSharedPref(userProfile: UserProfile, success: Boolean) {
        val sharedPref = getSharedPreferences("LoginPreference",
                MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("id", userProfile._id)
        editor.putString("firstName", userProfile.firstName)
        editor.putString("lastName", userProfile.lastName)
        editor.putString("image", "http://192.168.1.69:90/${userProfile.image}")
        editor.putString("contact", userProfile.contact)
        editor.putString("email", userProfile.email)
        editor.putString("password", binding.etPassword.text.toString())
        editor.putString("token", userProfile.token)
        editor.putBoolean("isAdmin", userProfile.isAdmin)
        editor.putBoolean("isSuccess", success)

        editor.apply()
    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        idSharedPref = sharedPref.getString("_id", "")
        emailSharedPref = sharedPref.getString("email", "")
        passwordSharedPref = sharedPref.getString("password", "")
        firstNameSharedPref = sharedPref.getString("firstName", "")
        lastNameSharedPref = sharedPref.getString("lastName", "")
        imageSharedPref = sharedPref.getString("image", "")
        contactSharedPref = sharedPref.getString("contact", "")
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)
        tokenSharedPref = sharedPref.getString("token", "")

    }

    /*----------------------------------CLEAR VIEWS---------------------------------------------------------------*/
    private fun clearFields() {
        binding.etFirstName.text.clear()
        binding.etLastName.text.clear()
        binding.etContact.text.clear()
        binding.etEmail.text.clear()
        binding.etUsername.text.clear()
        binding.etPassword.text.clear()
        binding.etConfirmPassword.text.clear()

    }

}