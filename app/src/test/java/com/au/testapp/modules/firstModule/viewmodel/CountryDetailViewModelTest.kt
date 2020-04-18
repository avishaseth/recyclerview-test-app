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

class CountryDetailViewModelTest {

    private val vmSpy = spyk<CountryDetailViewModel>(recordPrivateCalls = true)
    private lateinit var viewModel: CountryDetailViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var fetchCountryResult: MutableLiveData<Results>

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = spyk(CountryDetailViewModel(),recordPrivateCalls = true)
        every { vmSpy["loadResults"]() } returns fetchCountryResult
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

}