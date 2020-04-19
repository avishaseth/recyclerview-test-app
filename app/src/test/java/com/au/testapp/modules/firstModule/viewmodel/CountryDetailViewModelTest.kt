package com.au.testapp.modules.firstModule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.au.testapp.modules.firstModule.model.Results
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.lang.reflect.Field

class CountryDetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var fetchCountryResult:MutableLiveData<Results>
    private val vmSpy = spyk<CountryDetailViewModel>(recordPrivateCalls = true)
    private var reflection: Field = CountryDetailViewModel::class.java.getDeclaredField("mResults")

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { vmSpy["loadResults"]() } returns fetchCountryResult
        reflection.isAccessible=true
    }

    /**
     * The purpose for this test case is to check if the method
     * will run loadResults() if forceLoad value is true
     *
     * in order to check the case the requirement is
     * forceLoad = true
     */
    @Test
    fun fetchCountryList_forceLoadTrue_shouldCallLoadResults() {
        vmSpy.fetchCountryList(true)
        verify {vmSpy["loadResults"]() }
    }

    /**
     * The purpose for this test case is to check if the method
     * will run loadResults() if mResults.value is null
     *
     * in order to check the case the requirements are
     * forceLoad = false, mResults is not null, mResult.value is null
     */
    @Test
    fun fetchCountryList_mResultValueNull_shouldCallLoadResults(){
        every { fetchCountryResult.value } returns null
        reflection.set(vmSpy,fetchCountryResult)
        vmSpy.fetchCountryList(false)
        verify {vmSpy["loadResults"]() }
    }

    /**
     * The purpose for this test case is to check if the method
     * will run loadResults() if mResults is null
     *
     * in order to check the case the requirements are
     * forceLoad = false and mResults is null
     */
    @Test
    fun fetchCountryList_mResultNull_shouldCallLoadResults(){
        vmSpy.fetchCountryList(false)
        verify {vmSpy["loadResults"]() }
    }

    /**
     * The purpose for this test case is to check if the method
     * will never run loadResults() because forceLoad is false
     *
     * in order to check the case the requirement is
     * forceLoad = false and mResult is not null
     */
    @Test
    fun fetchCountryList_forceLoadFalse_shouldNeverCallLoadResults(){
        reflection.set(vmSpy,fetchCountryResult)
        every { fetchCountryResult.value } returns mockkClass(Results::class)
        vmSpy.fetchCountryList(false)
        verify(exactly = 0) {vmSpy["loadResults"]() }
    }
}