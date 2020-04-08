package com.au.testapp.modules.firstModule.network

import com.au.testapp.modules.firstModule.model.Results
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interface to provide the list of api calls to implement via retroFit.
 */
interface CountryDetailsService {
    /* Get the country details from the network */
    @get:GET("facts.json")
    val countryDetailsList: Call<Results>
}
