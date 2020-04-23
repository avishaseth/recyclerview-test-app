package com.au.testapp.modules.firstModule.network

import androidx.lifecycle.MutableLiveData
import com.au.testapp.modules.firstModule.model.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

/**
 * Class to provide the implementation for Retrofit to make the api calls.
 */
class CountryDetailsRepository(type: Type, typeAdapter: Any) {

    private var mMutableResults: MutableLiveData<Results> = MutableLiveData()

    private val mApi: CountryDetailsService = CountryDetailsService.create(type, typeAdapter)

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
                    mMutableResults.postValue(results)
                }

                override fun onFailure(call: Call<Results>, t: Throwable) {
                    var results = Results()
                    results.rows = null
                    results.isApiSuccess = false
                    results.resultCode = ResponseCode.APPLICATION_NETWORK_ERROR_GENERIC
                    mMutableResults.postValue(results)
                }
            })

            return mMutableResults
        }

}
