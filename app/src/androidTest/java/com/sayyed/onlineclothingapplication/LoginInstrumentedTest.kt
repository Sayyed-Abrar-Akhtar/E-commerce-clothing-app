package com.sayyed.onlineclothingapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sayyed.onlineclothingapplication.ui.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@LargeTest
@RunWith(JUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLoginUI() {
        Espresso.onView(ViewMatchers.withId(R.id.etUsername))
            .perform(ViewActions.typeText("akhtars10@uni.coventry.ac.uk"))

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.etPassword))
            .perform(ViewActions.typeText("rehman"))

        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
            .perform(ViewActions.click())

        Thread.sleep(5000)
        Espresso.onView(ViewMatchers.withId(R.id.recyclerViewCategory))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}