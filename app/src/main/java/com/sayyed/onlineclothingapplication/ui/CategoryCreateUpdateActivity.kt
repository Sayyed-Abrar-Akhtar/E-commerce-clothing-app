package com.sayyed.onlineclothingapplication.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityCategoryCreateUpdateBinding
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.response.UploadResponse
import com.sayyed.onlineclothingapplication.utils.FileUpload
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModel
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class CategoryCreateUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryCreateUpdateBinding
    private lateinit var categoryViewModel: CategoryViewModel

    private lateinit var navigationDrawerSetup: NavigationDrawerSetup
    private lateinit var toggle: ActionBarDrawerToggle



    private lateinit var listCategory: MutableList<String>

    private var firstNameSharedPref : String? = ""
    private var lastNameSharedPref : String? = ""
    private var imageSharedPref : String? = ""
    private  var contactSharedPref : String? = ""
    private  var tokenSharedPref : String? =  ""
    private  var isAdminSharedPref : Boolean = false

    private  var header : String? = ""
    private var idIntent: String = ""
    private var nameIntent: String = ""
    private var imageIntent: String = ""



    private var REQUEST_GALLERY_CODE = 1
    private var REQUEST_CAMERA_CODE = 0
    private var imageUrl: String? = null

    private var isSuccessfulUploadImage: Boolean =  false
    private var isSuccess: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_create_update)


        binding = DataBindingUtil.setContentView(this@CategoryCreateUpdateActivity, R.layout.activity_category_create_update)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()

        /*----------------------------------------GET INTENT DATA-------------------------------------------------*/

        idIntent = intent.getStringExtra("idIntent").toString()
        nameIntent = intent.getStringExtra("nameIntent").toString()
        imageIntent = intent.getStringExtra("imageIntent").toString()



        header = intent.getStringExtra("header")


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

        navigationDrawerSetup.navDrawerLayoutInitialization(binding.tvToolbarTitle, "$header")

        navigationDrawerSetup.addHeaderText(
            this@CategoryCreateUpdateActivity,
            binding.navigationView,
            "$firstNameSharedPref $lastNameSharedPref",
            "$contactSharedPref",
            "$imageSharedPref",
        )
        navigationDrawerSetup.addEventListenerToNavItems(
            this@CategoryCreateUpdateActivity,
            binding.navigationView,
            isAdminSharedPref
        )


        setupViewModel()

        setupUI()



        binding.imgCategory.setOnClickListener{

            FileUpload.loadPopUpMenu(
                this@CategoryCreateUpdateActivity,
                this@CategoryCreateUpdateActivity,
                binding.imgCategory
            )
        }

        binding.btnCreateAndUpdate.setOnClickListener {
            if (header == "Add Category") {
               createCategoryObserver(imageIntent)
            }
            updateCategoryObserver(imageIntent)
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
                binding.imgCategory.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
                setupImageUploadObservers()

            } else if (requestCode == REQUEST_CAMERA_CODE) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = FileUpload.bitmapToFile(imageBitmap, getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString(),"$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                binding.imgCategory.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                setupImageUploadObservers()
            }
        }
    }

    /*-------------------------------------SET DATA FROM API TO DISPLAY-------------------------------------------*/
    private fun setupImageUploadObservers() {
        val body = FileUpload.setMimeType(imageUrl)
        categoryViewModel.uploadImage(body).observe(this, {
            it.apiUploadCall()
        })
    }


    /*----------------------------GET SHARED PREFERENCES---------------------------------------------------------*/
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("LoginPreference", MODE_PRIVATE)
        firstNameSharedPref = sharedPref.getString("firstName", "")
        lastNameSharedPref = sharedPref.getString("lastName", "")
        imageSharedPref = sharedPref.getString("image", "")
        contactSharedPref = sharedPref.getString("contact", "")
        tokenSharedPref = sharedPref.getString("token", "")
        isAdminSharedPref = sharedPref.getBoolean("isAdmin", false)

    }

    /*----------------------------SET UP UI-----------------------------------------------------------------------*/
    private fun setupUI() {
        listCategory = mutableListOf()
        binding.etCategoryName.setText(nameIntent)
        if(imageIntent != "" ) {
            Glide.with(this@CategoryCreateUpdateActivity)
                .load(FileUpload.checkImageString(imageIntent))
                .into(binding.imgCategory)
        }
        binding.btnCreateAndUpdate.text = header
    }

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val categoryDAO: CategoryDAO = OnlineClothingDB.getInstance(application).categoryDAO
        val repository =  CategoryRepository(categoryDAO)
        val factory = CategoryViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, factory).get(CategoryViewModel::class.java)
        categoryViewModel.insertCategoryIntoRoom()
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
                            imageIntent = image
                        }
                        Log.i("USER-TAG", "==>LOADED USER PROFILE FROM API ==> $data")
                    }
                }
                Status.ERROR -> {
                    println("=========================ERROR====================")
                    println(resource.data)
                    println(resource.message)
                    println("==================================================")
                }
                Status.LOADING -> {
                    println("=========================LOADER====================")
                    println("!!! LOADING... !!!")
                    println("===================================================")
                }
            }
        }
    }

    /*-----------------------------------------CREATE NEW PRODUCT-------------------------------------------------*/
    private fun createCategoryObserver(image: String){
        val name: String = binding.etCategoryName.text.toString()

        categoryViewModel.createCategory(
            "Bearer $tokenSharedPref",
            name,
            image
        ).observe(this, {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { category ->
                            if(category.success) {
                                clearFields()
                                binding.btnCreateAndUpdate.text = getString(R.string.loading)
                                Toast.makeText(this@CategoryCreateUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@CategoryCreateUpdateActivity, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                    Status.ERROR -> {
                        println("=========================ERROR====================")
                        println(resource.data)
                        println(resource.message)
                        println("==================================================")
                    }
                    Status.LOADING -> {
                        println("=========================LOADER====================")
                        println("!!! LOADING... !!!")
                        println("===================================================")
                    }

                }
            }
        })
    }


    /*-----------------------------------------CREATE NEW PRODUCT-------------------------------------------------*/
    private fun updateCategoryObserver(image: String){
        val name: String = binding.etCategoryName.text.toString()

        categoryViewModel.updateCategory(
            "Bearer $tokenSharedPref",
            idIntent,
            name,
            image
        ).observe(this, {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { product ->
                            if(product.success) {
                                clearFields()
                                binding.btnCreateAndUpdate.text = getString(R.string.loading)
                                Toast.makeText(this@CategoryCreateUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@CategoryCreateUpdateActivity, ProductActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                    Status.ERROR -> {
                        println("=========================ERROR====================")
                        println(resource.data)
                        println(resource.message)
                        println("==================================================")
                    }
                    Status.LOADING -> {
                        println("=========================LOADER====================")
                        println("!!! LOADING... !!!")
                        println("===================================================")
                    }

                }
            }
        })
    }

    private fun clearFields() {
        binding.etCategoryName.text.clear()
    }
}