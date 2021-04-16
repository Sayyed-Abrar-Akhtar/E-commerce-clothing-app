package com.sayyed.onlineclothingapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sayyed.onlineclothingapplication.ui.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@LargeTest
@RunWith(JUnit4::class)
class ProductDetailInstrumentedTest {

    @get:Rule
    val testRule = ActivityScenarioRule(AdminActivity::class.java)

    @Test
    fun checkProductUI() {
        Thread.sleep(1500)
        Espresso.onView(ViewMatchers.withId(R.id.btnAllProduct))
                .perform(ViewActions.click())

        Thread.sleep(1500)
        Espresso.onView(ViewMatchers.withId(R.id.recyclerViewProductAdmin))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}