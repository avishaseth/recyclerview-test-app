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

    private val vmSpy = spyk<CountryDetailViewModel>(recordPrivateCalls = true)
    private lateinit var viewModel: CountryDetailViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var fetchCountryResult:MutableLiveData<Results>
    var reflection: Field = CountryDetailViewModel::class.java.getDeclaredField("mResults")

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        viewModel = spyk(CountryDetailViewModel(),recordPrivateCalls = true)
        every { vmSpy["loadResults"]() } returns fetchCountryResult
        reflection.isAccessible=true
    }

    @Test
    fun fetchCountryList_forceLoadTrue_shouldCallLoadResults() {
        vmSpy.fetchCountryList(true)
        verify {vmSpy["loadResults"]() }
    }

    @Test
    fun fetchCountryList_mResultNull_shouldCallLoadResults(){
        vmSpy.fetchCountryList(false)
        verify {vmSpy["loadResults"]() }
    }

    @Test
    fun fetchCountryList_forceLoadFalse_shouldNeverCallLoadResults(){
        reflection.set(vmSpy,fetchCountryResult)
        every { fetchCountryResult.value } returns mockkClass(Results::class)
        vmSpy.fetchCountryList(false)
        verify(exactly = 0) {vmSpy["loadResults"]() }
    }
}