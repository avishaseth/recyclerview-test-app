package com.au.testapp.modules.firstModule.activities

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.au.testapp.IdlingResourceSingleton
import com.au.testapp.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryDetailActivityTest {

    @get:Rule
    val activityRule2 = ActivityTestRule(CountryDetailActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(IdlingResourceSingleton.countingIdlingResource)

    }

    @Test
    fun actionbar_notNull() {
        onView(withId(R.id.toolbar))
            .check(ViewAssertions.matches(hasDescendant(withText("About Canada"))))

    }

    @Test
    fun recyclerview_isDisplayed() {
        onView(withId(R.id.recycler_view))
            .check(ViewAssertions.matches(isDisplayed()))

    }

    @Test
    fun recyclerview_hasChildWithValue() {
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Housing")), ViewActions.click()
                ))

    }

    /*  @Test
    fun screenState_onRotation() {
        activityRule2.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    }*/

    @Test
    fun screenState_noError() {
        onView(withId(R.id.text_error)).check(ViewAssertions.doesNotExist())

    }

/*  @Test
    fun screenState_withError() {
        onView(withId(R.id.text_error)).check(ViewAssertions.matches(isDisplayed()))

    }*/

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResourceSingleton.countingIdlingResource)
    }

}