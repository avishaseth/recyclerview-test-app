package com.au.testapp.modules.firstModule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.au.testapp.modules.firstModule.gsonbuilder.GetCountryDetailsListDeserializer
import com.au.testapp.modules.firstModule.model.Results
import com.au.testapp.modules.firstModule.network.CountryDetailsRepository
import com.google.gson.reflect.TypeToken

/**
 * View Model to access the country detail list from network
 */
class CountryDetailViewModel : ViewModel() {

    private lateinit var mCountryDetailsListDataSource: CountryDetailsRepository

    /* Data which is observed by the Activity and all the related fragments */
    private var mResults: MutableLiveData<Results>? = null

    init {
        injectDependencies()
    }

    private fun injectDependencies() {
        mCountryDetailsListDataSource = CountryDetailsRepository(object : TypeToken<Results>() {
        }.type, GetCountryDetailsListDeserializer())
    }

    /* Get the country details list results */
    fun fetchCountryList(forceLoad: Boolean): MutableLiveData<Results> {
        if (forceLoad || mResults == null || mResults!!.value == null) {
            mResults = loadResults()
        }
        return mResults!!
    }

    /* Load the results from the network */
    private fun loadResults(): MutableLiveData<Results> {
        return mCountryDetailsListDataSource.countryDetailsList
    }
}
