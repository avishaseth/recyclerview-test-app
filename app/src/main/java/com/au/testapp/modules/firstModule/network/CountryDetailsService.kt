package com.au.testapp.modules.firstModule.network

import com.au.testapp.modules.firstModule.model.Results
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/**
 * Interface to provide the list of api calls to implement via retroFit.
 */
interface CountryDetailsService {

    /* Get the country details from the network */
    @get:GET("facts.json")
    val countryDetailsList: Call<Results>

    companion object Factory {
        //Base URL
        private const val COUNTRY_DETAIL_LIST_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/"

        fun create(type: Type, typeAdapter: Any): CountryDetailsService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(createGSONConverter(type, typeAdapter))
                .baseUrl(COUNTRY_DETAIL_LIST_URL)
                .build()

            return retrofit.create(CountryDetailsService::class.java)
        }

        private fun createGSONConverter(type: Type, typeAdapter: Any): Converter.Factory {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(type, typeAdapter)
            val gson = gsonBuilder.create()
            return GsonConverterFactory.create(gson)
        }
    }
}
