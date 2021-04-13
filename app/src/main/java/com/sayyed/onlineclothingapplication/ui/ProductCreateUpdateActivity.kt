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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.adapter.ProductAdapter
import com.sayyed.onlineclothingapplication.dao.CategoryDAO
import com.sayyed.onlineclothingapplication.dao.ProductDAO
import com.sayyed.onlineclothingapplication.database.OnlineClothingDB
import com.sayyed.onlineclothingapplication.databinding.ActivityProductCreateUpdateBinding
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.models.Product
import com.sayyed.onlineclothingapplication.repository.CategoryRepository
import com.sayyed.onlineclothingapplication.repository.ProductRepository
import com.sayyed.onlineclothingapplication.response.CategoryIdResponse
import com.sayyed.onlineclothingapplication.response.CategoryNameResponse
import com.sayyed.onlineclothingapplication.response.CategoryResponse
import com.sayyed.onlineclothingapplication.response.UploadResponse
import com.sayyed.onlineclothingapplication.utils.FileUpload
import com.sayyed.onlineclothingapplication.utils.Resource
import com.sayyed.onlineclothingapplication.utils.Status
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModel
import com.sayyed.onlineclothingapplication.viewmodel.CategoryViewModelFactory
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModel
import com.sayyed.onlineclothingapplication.viewmodel.ProductViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductCreateUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductCreateUpdateBinding
    private lateinit var productViewModel: ProductViewModel
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


    private var nameIntent: String = ""
    private var priceIntent: String = ""
    private var idIntent: String = ""
    private var imageIntent: String = ""
    private var brandIntent: String = ""
    private var categoryIntent: String = ""
    private var descriptionIntent: String = ""
    private var countInStockIntent: String = ""

    private var REQUEST_GALLERY_CODE = 1
    private var REQUEST_CAMERA_CODE = 0
    private var imageUrl: String? = null

    private var isSuccessfulUploadImage: Boolean =  false




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_create_update)

        binding = DataBindingUtil.setContentView(this@ProductCreateUpdateActivity, R.layout.activity_product_create_update)

        /*----------------------------------------SHARED PREFERENCES----------------------------------------------*/
        getSharedPref()

        /*----------------------------------------GET INTENT DATA-------------------------------------------------*/

        nameIntent = intent.getStringExtra("nameIntent").toString()
        idIntent = intent.getStringExtra("idIntent").toString()
        priceIntent = intent.getStringExtra("priceIntent").toString()
        brandIntent = intent.getStringExtra("brandIntent").toString()
        descriptionIntent = intent.getStringExtra("descriptionIntent").toString()
        countInStockIntent = intent.getStringExtra("countInStockIntent").toString()
        imageIntent = intent.getStringExtra("imageIntent").toString()

        Glide.with(this@ProductCreateUpdateActivity)
                .load(FileUpload.checkImageString(imageIntent))
                .into(binding.imgProduct)


        header = intent.getStringExtra("header")
        binding.btnCreateAndUpdate.text = header

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
            this@ProductCreateUpdateActivity,
            binding.navigationView,
            "$firstNameSharedPref $lastNameSharedPref",
            "$contactSharedPref",
            "$imageSharedPref",
        )
        navigationDrawerSetup.addEventListenerToNavItems(this@ProductCreateUpdateActivity, binding.navigationView, isAdminSharedPref)


        setupViewModel()

        setupUI()

        setupCategoryNameObservers()

        setupSpinner(listCategory)

        binding.imgProduct.setOnClickListener{

            FileUpload.loadPopUpMenu(this@ProductCreateUpdateActivity, this@ProductCreateUpdateActivity, binding.imgProduct)

            //setupImageUploadObservers()
        }

        binding.btnCreateAndUpdate.setOnClickListener {
            updateProductObserver(imageIntent)
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
                binding.imgProduct.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
                setupImageUploadObservers()

            } else if (requestCode == REQUEST_CAMERA_CODE) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = FileUpload.bitmapToFile(imageBitmap, getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        .toString(),"$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                binding.imgProduct.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                setupImageUploadObservers()
            }
        }
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
        binding.etProductName.setText(nameIntent)
        binding.etProductPrice.setText(priceIntent)
        binding.etProductBrand.setText(brandIntent)
        binding.etProductDescription.setText(descriptionIntent)
        binding.etProductStock.setText(countInStockIntent)
    }


    /*----------------------------SET UP SPINNER------------------------------------------------------------------*/
    private fun setupSpinner(listCategory: MutableList<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listCategory)
        binding.spinnerCategory.adapter = adapter
        binding.spinnerCategory.onItemSelectedListener =
        object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                setupCategoryIdObservers(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    /*--------------------------------------------SET UP VIEW MODEL-----------------------------------------------*/
    private fun setupViewModel() {
        val productDao: ProductDAO = OnlineClothingDB.getInstance(application).productDAO
        val repository = ProductRepository(productDao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        productViewModel.insertProductToRoom()

        val categoryDAO: CategoryDAO = OnlineClothingDB.getInstance(application).categoryDAO
        val categoryRepository = CategoryRepository(categoryDAO)
        val categoryFactory = CategoryViewModelFactory(categoryRepository)
        categoryViewModel = ViewModelProvider(this, categoryFactory).get(CategoryViewModel::class.java)
    }

    /*-------------------------------------SET DATA FROM API TO DISPLAY-------------------------------------------*/
    private fun setupImageUploadObservers() {
        val body = FileUpload.setMimeType(imageUrl)
        productViewModel.uploadImage(body).observe(this, {
            it.apiUploadCall()
        })
    }


    /*-------------------------------------SET DATA FROM API TO DISPLAY-------------------------------------------*/
    private fun setupCategoryNameObservers() {
        categoryViewModel.getCategoryName().observe(this, {
            it.loadApiData()
        })
    }

    /*-------------------------------------SET DATA FROM API TO DISPLAY-------------------------------------------*/
    private fun setupCategoryIdObservers(name:String) {
        categoryViewModel.getCategoryId(name).observe(this, {
            it.loadApiCategoryId()
        })
    }


    /*-----------------------------------------CREATE NEW PRODUCT-------------------------------------------------*/
    private fun updateProductObserver(image: String){
        val id: String = idIntent
        val name: String = binding.etProductName.text.toString()
        val price: Double = binding.etProductPrice.text.toString().toDouble()
        val description: String = binding.etProductDescription.text.toString()
        val brand: String = binding.etProductBrand.text.toString()
        val qty: Int = binding.etProductStock.text.toString().toInt()


        productViewModel.updateProduct(
            "Bearer $tokenSharedPref",
                id,
                name,
                price,
                description,
                image,
                brand,
                categoryIntent,
                qty
        ).observe(this, {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { product ->
                            if(product.success) {
                                clearFields()
                                Toast.makeText(this@ProductCreateUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@ProductCreateUpdateActivity, ProductActivity::class.java)
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


    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<CategoryNameResponse>.loadApiData() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {

                    resource.data?.let { category ->
                        listCategory.clear()
                        listCategory.addAll(category.category)
                        setupSpinner(listCategory)

                        Log.i("CategoryAdminTAG", "==>LOADED CATEGORY DATA FROM API")
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

    /*-------------------------------------GET DATA FROM API------------------------------------------------------*/
    private fun Resource<CategoryIdResponse>.loadApiCategoryId() {
        let { resource ->
            when (resource.status ) {
                Status.SUCCESS -> {
                    resource.data?.let {
                        categoryIntent = it.category
                        Log.i("CategoryAdminTAG", "==>LOADED CATEGORY DATA FROM API")
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

    private fun clearFields() {
        binding.etProductName.text.clear()
        binding.etProductStock.text.clear()
        binding.etProductDescription.text.clear()
        binding.etProductBrand.text.clear()
        binding.etProductPrice.text.clear()
    }
}