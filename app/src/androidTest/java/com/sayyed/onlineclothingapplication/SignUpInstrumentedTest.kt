package com.sayyed.onlineclothingapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sayyed.onlineclothingapplication.ui.LoginActivity
import com.sayyed.onlineclothingapplication.ui.SignUpActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@LargeTest
@RunWith(JUnit4::class)
class SignUpInstrumentedTest {
    @get:Rule
    val testRule = ActivityScenarioRule(SignUpActivity::class.java)

    @Test
    fun checkSignUpUI() {
        Espresso.onView(ViewMatchers.withId(R.id.etFirstName))
                .perform(ViewActions.typeText("Aasif"))

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.etLastName))
                .perform(ViewActions.typeText("Ahmed"))

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.etContact))
                .perform(ViewActions.typeText("009798685525555"))

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.etEmail))
                .perform(ViewActions.typeText("aasif@ahmed.com"))

        Thread.sleep(1000)


        Espresso.onView(ViewMatchers.withId(R.id.etUsername))
                .perform(ViewActions.typeText("aasif"))

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.etPassword))
                .perform(ViewActions.typeText("aasif"))

        Thread.sleep(1000)

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.etConfirmPassword))
                .perform(ViewActions.typeText("aasif"))

        Thread.sleep(1000)

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btnSignUp))
                .perform(ViewActions.click())

        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(R.id.recyclerViewCategory))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}