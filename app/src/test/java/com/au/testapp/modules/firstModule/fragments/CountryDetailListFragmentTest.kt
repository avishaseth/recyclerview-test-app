package com.au.testapp.modules.firstModule.fragments

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.au.testapp.modules.firstModule.activities.CountryDetailActivity
import com.au.testapp.modules.firstModule.model.Results
import com.au.testapp.modules.firstModule.viewmodel.CountryDetailViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.lang.reflect.Field

class CountryDetailListFragmentTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    private lateinit var mockedActivity: CountryDetailActivity
    @MockK(relaxed = true)
    private lateinit var mockedViewModel: CountryDetailViewModel
    @MockK(relaxed = true)
    private lateinit var mockedSwipe: SwipeRefreshLayout
    @MockK(relaxed = true)
    private lateinit var mockedObserver: Observer<Results>
    @MockK(relaxed = true)
    private lateinit var mockedBundle: Bundle

    private lateinit var fragmentTest: CountryDetailListFragment
    private lateinit var fragmentTestSpy: CountryDetailListFragment

    private var reflectionVm: Field =
        CountryDetailListFragment::class.java.getDeclaredField("mViewModel")
    private var reflectionActivity: Field =
        CountryDetailListFragment::class.java.getDeclaredField("mActivity")
    private var reflectionSwipe: Field =
        CountryDetailListFragment::class.java.getDeclaredField("mSwipeRefreshLayout")
    private var reflectionObserver: Field =
        CountryDetailListFragment::class.java.getDeclaredField("mGetCountryDetailsListObserver")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fragmentTest = CountryDetailListFragment()
        fragmentTestSpy = spyk<CountryDetailListFragment>(recordPrivateCalls = true)
        reflectionActivity.isAccessible = true
        reflectionSwipe.isAccessible = true
        reflectionVm.isAccessible = true
        reflectionObserver.isAccessible = true
        reflectionActivity.set(fragmentTestSpy, mockedActivity)
        reflectionVm.set(fragmentTestSpy, mockedViewModel)
        reflectionSwipe.set(fragmentTestSpy, mockedSwipe)
        reflectionObserver.set(fragmentTestSpy, mockedObserver)
    }

    /**
     * The purpose for this test case is to check
     * fragment never called viewModel.fetchCountryList
     * if activity.fetchCountryList result is false
     */
    @Test
    fun onRefresh_isSuccessFalse() {
        every { mockedActivity.fetchCountryDetailsList(any()) } returns false
        fragmentTestSpy.onRefresh()
        verify(exactly = 0) { mockedViewModel.fetchCountryList(any()) }
        verify { mockedSwipe.isRefreshing = any() }
    }

    /**
     * The purpose for this test case is to check
     * fragment to cal viewModel.fetchCountryList
     * if activity.fetchCountryList result is true
     */
    @Test
    fun onRefresh_isSuccessTrue() {
        every { mockedActivity.fetchCountryDetailsList(any()) } returns true
        fragmentTestSpy.onRefresh()
        verify { mockedViewModel.fetchCountryList(any()) }
        verify(exactly = 0) { mockedSwipe.isRefreshing = any() }
    }
}