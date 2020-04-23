package com.au.testapp.modules.firstModule.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.au.testapp.modules.firstModule.gsonbuilder.GetCountryDetailsListDeserializer
import com.au.testapp.modules.firstModule.model.Results
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field
import java.lang.reflect.Type

class CountryDetailsRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var mockApiService: CountryDetailsService
    @MockK(relaxed = true)
    private lateinit var type: Type
    @MockK(relaxed = true)
    private lateinit var mockDeserializer: GetCountryDetailsListDeserializer
    @MockK(relaxed = true)
    private lateinit var mockResponse: Call<Results>

    private lateinit var repo: CountryDetailsRepository
    private lateinit var repoSpy: CountryDetailsRepository
    private var mockMutableResult = MutableLiveData<Results>()

    private var reflectionMr: Field = CountryDetailsRepository::class.java.getDeclaredField("mMutableResults")
    private val callbackSlot = slot<Callback<Results>>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(CountryDetailsService)
        every { CountryDetailsService.create(any(), any()) } returns mockApiService
        repo = CountryDetailsRepository(type, mockDeserializer)
        repoSpy = spyk(repo)
        reflectionMr.isAccessible = true
        reflectionMr.set(repoSpy, mockMutableResult)
    }

    /**
     * The purpose for this test case is to check
     * the results value from server is null
     */
    @Test
    fun getCountryDetailsList_successResponse_resultsNull() {
        val response: Response<Results> = Response.success(null)
        response.code()
        every { mockApiService.countryDetailsList } returns mockResponse
        every { mockResponse.enqueue(capture(callbackSlot)) } answers {
            callbackSlot.captured.onResponse(mockResponse, response)
        }
        repoSpy.countryDetailsList
        assert(mockMutableResult.value?.resultCode == 200)
        assert(mockMutableResult.value?.rows == null)
        assert(mockMutableResult.value?.isApiSuccess == false)
    }

    /**
     * The purpose for this test case is to check
     * the results value from server is not null
     */
    @Test
    fun getCountryDetailsList_successResponse_resultsNotNull() {
        val results = Results()
        results.title = "unit test"
        val response: Response<Results> = Response.success(results)
        response.code()
        every { mockApiService.countryDetailsList } returns mockResponse
        every { mockResponse.enqueue(capture(callbackSlot)) } answers {
            callbackSlot.captured.onResponse(mockResponse, response)
        }
        repoSpy.countryDetailsList
        assert(mockMutableResult.value?.resultCode == 200)
        assert(mockMutableResult.value == results)
        assert(mockMutableResult.value?.isApiSuccess == true)
    }

    /**
     * The purpose for this test case is to check
     * the results value from server is error response
     */
    @Test
    fun getCountryDetailsList_errorResponse() {
        val results = Results()
        results.title = "unit test"
        val response: Response<Results> = Response.success(results)
        response.code()
        every { mockApiService.countryDetailsList } returns mockResponse
        every { mockResponse.enqueue(capture(callbackSlot)) } answers {
            callbackSlot.captured.onFailure(mockResponse, Throwable())
        }
        repoSpy.countryDetailsList
        assert(mockMutableResult.value?.resultCode == ResponseCode.APPLICATION_NETWORK_ERROR_GENERIC)
        assert(mockMutableResult.value?.rows == null)
        assert(mockMutableResult.value?.isApiSuccess == false)
    }

}