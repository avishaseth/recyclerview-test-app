package com.au.testapp

import androidx.test.espresso.idling.CountingIdlingResource

/*
    This object is used for CountingIdlingResource
    for informing Espresso to wait until network call is completed.
 */
object IdlingResourceSingleton {
    private const val RESOURCE = "GLOBAL"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}