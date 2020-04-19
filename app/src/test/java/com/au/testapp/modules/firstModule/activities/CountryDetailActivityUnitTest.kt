package com.au.testapp.modules.firstModule.activities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.au.testapp.modules.firstModule.model.Results
import com.au.testapp.modules.firstModule.viewmodel.CountryDetailViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers
import java.lang.reflect.Field

class CountryDetailActivityUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    private lateinit var activityTest: CountryDetailActivity
    private lateinit var activitySpy : CountryDetailActivity
    private lateinit var mockedObserver: Observer<Results>
    //@MockK
    private lateinit var mockedViewModel: CountryDetailViewModel
    @MockK
    private lateinit var resultsLiveData: MutableLiveData<Results>
    private var reflectionVm: Field = CountryDetailActivity::class.java.getDeclaredField("mViewModel")
    private var reflectionObserver: Field = CountryDetailActivity::class.java.getDeclaredField("mGetCountryDetailsListObserver")

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockedViewModel = mockk<CountryDetailViewModel>(relaxed = true)
        mockedObserver = mockk<Observer<Results>>()
        activityTest= CountryDetailActivity()
        activitySpy = spyk(activityTest)
        reflectionVm.isAccessible = true
        reflectionObserver.isAccessible = true
        reflectionVm.set(activitySpy, mockedViewModel)
    }

    /**
     * The purpose for this test case is to check
     * activity never called fetchCountryList if there is no internet
     */
    @Test
    fun fetchCountryDetailsList_noInternet_shouldNeverFetch() {
        reflectionObserver.set(activitySpy, mockedObserver)
        every { activitySpy.isNetworkAvailable() } returns false
        every { activitySpy.showToast(any()) } just runs
        every { mockedViewModel.fetchCountryList(ArgumentMatchers.anyBoolean()) } returns resultsLiveData
        val result = activitySpy.fetchCountryDetailsList(true)
        verify(exactly = 0) {mockedViewModel.fetchCountryList(true) }
        assertFalse(result)

    }

    /**
     * The purpose for this test case is to check
     * activity called fetchCountryList if there  internet available
     */
    @Test
    fun fetchCountryDetailsList_hasInternet_shouldFetch() {
        reflectionObserver.set(activitySpy, mockedObserver)
        every { activitySpy.isNetworkAvailable() } returns true
        every { mockedViewModel.fetchCountryList(any()) } returns resultsLiveData
        val result = activitySpy.fetchCountryDetailsList(true)
        verify{ mockedViewModel.fetchCountryList(any()) }
        assertTrue(result)
    }
}