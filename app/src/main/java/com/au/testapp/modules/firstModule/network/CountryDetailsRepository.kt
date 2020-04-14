package com.au.testapp.modules.firstModule.network

import androidx.lifecycle.MutableLiveData
import com.au.testapp.modules.firstModule.model.Results
import com.google.gson.GsonBuilder
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/**
 * Class to provide the implementation for Retrofit to make the api calls.
 */
class CountryDetailsRepository(type: Type, typeAdapter: Any) {

    companion object {
        /* Base url for the country details API */
        private val COUNTRY_DETAIL_LIST_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/"

        private fun createGSONConverter(type: Type, typeAdapter: Any): Converter.Factory {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(type, typeAdapter)
            val gson = gsonBuilder.create()
            return GsonConverterFactory.create(gson)
        }
    }

    private var mMutableResults: MutableLiveData<Results>
    private val mApi: CountryDetailsService
    private val mRetrofitInstance: Retrofit

    init {
        mRetrofitInstance = Retrofit.Builder()
            .baseUrl(COUNTRY_DETAIL_LIST_URL)
            .addConverterFactory(createGSONConverter(type, typeAdapter))
            .build()

        this.mApi = mRetrofitInstance.create(CountryDetailsService::class.java)
        mMutableResults = MutableLiveData()
    }

    /* Get the country list from the network
     @return MutableLiveData<Results> */
    val countryDetailsList: MutableLiveData<Results>
        get() {
            val call = mApi.countryDetailsList

            call.enqueue(object : Callback<Results> {
                override fun onResponse(call: Call<Results>, response: Response<Results>) {
                    var results = Results()
                    if (response.body() == null) {
                        results.rows = null
                        results.isApiSuccess = false
                    } else {
                        results = response.body()!!
                        results.isApiSuccess = true
                    }
                    // TODO- We need to map the network/ application specific errors here and in ResponseCode.class.
                    // Need to do the the handling of the error in ErrorHandler class.
                    results.resultCode = response.code()
                    mMutableResults.value = results
                }

                override fun onFailure(call: Call<Results>, t: Throwable) {
                    var results = Results()
                    results.rows = null
                    results.isApiSuccess = false
                    results.resultCode = ResponseCode.APPLICATION_NETWORK_ERROR_GENERIC
                    mMutableResults.value = results
                }
            })

            return mMutableResults
        }

}
