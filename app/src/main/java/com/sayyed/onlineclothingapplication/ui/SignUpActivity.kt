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
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.databinding.ActivitySignUpBinding
import com.sayyed.onlineclothingapplication.entities.User
import com.sayyed.onlineclothingapplication.models.UserProfile
import com.sayyed.onlineclothingapplication.repository.UserRepository
import com.sayyed.onlineclothingapplication.response.UploadResponse
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

    private var headerText: String = "Create New Account"

    private lateinit var userViewModel: UserViewModel


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
    private var isSuccessfulUserProfile: Boolean =  false
    private var isSuccessfulUploadImage: Boolean =  false
    private var isLoading: Boolean =  false

    private var REQUEST_GALLERY_CODE = 1
    private var REQUEST_CAMERA_CODE = 0
    private var imageUrl: String? = null

    private lateinit var firstNameField: String
    private lateinit var lastNameField: String
    private lateinit var contactField: String
    private lateinit var usernameField: String
    private lateinit var emailField: String
    private lateinit var passwordField: String
    private lateinit var imageResponseFromApi: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        val btnText = intent.getStringExtra("btnText")

        /*--------------------------SETUP UPDATE SCREEN-----------------------------------------------------------*/
        if(btnText == "Update" || btnText == "update") {
            binding.btnSignUp.text = btnText
            initializeSharedPref()
            headerText = "Update"
            val glideImg= FileUpload.checkImageString("$imageSharedPref")

            Glide.with(this@SignUpActivity)
                    .load(glideImg)
                    .into(binding.imgUserProfile)

        } else {
            binding.btnSignUp.text = "Sign up"
        }

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
        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, headerText)
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

            if (binding.btnSignUp.text == "Update") {
                userViewModel.updateUser(
                        "Bearer $tokenSharedPref",
                        "${binding.etFirstName.text}",
                        "${binding.etLastName.text}",
                        "${binding.etContact.text}",
                        "${binding.etUsername.text}",
                        "$emailSharedPref",
                        "$passwordSharedPref",
                        "$imageSharedPref"
                ).observe(this@SignUpActivity, {
                    it.apiCall()
                })


                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({

                Toast.makeText(this@SignUpActivity, "User updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()

                    }, 800)
                }

            if (binding.btnSignUp.text == "Sign up") {
                userProfileDetail()
                val body = FileUpload.setMimeType(imageUrl)

                userViewModel.uploadImage(body).observe(this@SignUpActivity, {
                    it.apiUploadCall()
                })
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    if (isSuccessfulUploadImage) {
                        userViewModel.newAccount(
                                firstNameField,
                                lastNameField,
                                imageResponseFromApi,
                                contactField,
                                usernameField,
                                emailField,
                                passwordField
                        ).observe(this@SignUpActivity, {
                            it.apiCall()
                                if (isSuccessfulUserProfile) {
                                    Toast.makeText(this@SignUpActivity, "New user created successfully", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, DashboardActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        })
                    }
                }, 500)

            }
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

        setRequestBody(firstName, lastName, contact, username, email, password)

    }

    /*--------------------------------------SET UP REQUEST BODY---------------------------------------------------*/

    private fun setRequestBody(
            setFirstName: String,
            setLastName: String,
            setContact: String,
            setUsername: String,
            setEmail: String = "$emailSharedPref",
            setPassword: String = "$passwordSharedPref"
    ) {
        firstNameField = setFirstName
        lastNameField = setLastName
        contactField = setContact
        usernameField = setUsername
        emailField = setEmail
        passwordField = setPassword
    }


    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<UploadResponse>.apiUploadCall() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    resource.data?.let { data ->
                        val (success, image) = data
                        isSuccessfulUploadImage = success
                        if (success && image !== "") {
                            imageResponseFromApi = image
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



    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<UserResponse>.apiCall() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    resource.data?.let { data ->
                        val (success, userProfile) = data
                        isSuccessfulUserProfile = success
                        if (success && userProfile.token !== "") {
                            saveSharedPref(userProfile, success)
                            isLoading = false
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
        editor.putString("image", userProfile.image)
        editor.putString("contact", userProfile.contact)
        editor.putString("email", userProfile.email)
        editor.putString("username", userProfile.username)
        editor.putString("password", binding.etPassword.text.toString())
        editor.putString("token", userProfile.token)
        editor.putBoolean("isAdmin", userProfile.isAdmin)
        editor.putBoolean("isSuccess", success)

        editor.apply()
    }

    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun initializeSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)

        binding.etFirstName.setText(sharedPref.getString("firstName", ""))
        binding.etLastName.setText(sharedPref.getString("lastName", ""))
        binding.etContact.setText(sharedPref.getString("contact", ""))
        binding.etUsername.setText(sharedPref.getString("username", ""))
        binding.etPassword.setText(sharedPref.getString("password", ""))
        idSharedPref = sharedPref.getString("_id", "")
        imageSharedPref = sharedPref.getString("image", "")
        tokenSharedPref = sharedPref.getString("token", "")

        binding.etEmail.visibility = View.GONE
        binding.etConfirmPassword.visibility = View.GONE
        Glide.with(this@SignUpActivity).load(imageSharedPref).into(binding.imgUserProfile)
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