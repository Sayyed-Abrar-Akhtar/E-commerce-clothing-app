package com.sayyed.onlineclothingapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sayyed.onlineclothingapplication.ui.DashboardActivity
import com.sayyed.onlineclothingapplication.ui.SignUpActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class ProductInstrumentedTest {
    @get:Rule
    val testRule = ActivityScenarioRule(DashboardActivity::class.java)

    @Test
    fun checkProductUI() {
        Thread.sleep(1500)
        Espresso.onView(ViewMatchers.withId(R.id.recyclerViewCategory))
                .perform(ViewActions.click())

        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(R.id.recyclerViewProduct))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}