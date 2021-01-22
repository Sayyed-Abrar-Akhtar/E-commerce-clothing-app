package com.sayyed.onlineclothingapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sayyed.onlineclothingapplication.adapter.ViewPagerAdapter

class LoginActivity : AppCompatActivity() {

    private lateinit var listTitle: ArrayList<String>
    private lateinit var listFragments: ArrayList<Fragment>
    private lateinit var imageView: ImageView
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var viewpager: ViewPager2
    private lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewpager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabLayout)

        addList()

        val adapter =  ViewPagerAdapter(listFragments, supportFragmentManager, lifecycle)
        viewpager.adapter = adapter
        TabLayoutMediator(tabLayout, viewpager) {
            tab, position -> tab.text = listTitle[position]
        }.attach()


    }

    private fun addList() {
        listTitle = ArrayList<String>()
        listTitle.add("Login")
        listTitle.add("Sign Up")
        listFragments = ArrayList<Fragment>()
        listFragments.add(LoginFragment())
        listFragments.add(SignUpFragment())
    }
}


