package com.au.testapp.modules.firstModule.activities

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.au.testapp.BaseActivity
import com.au.testapp.IdlingResourceSingleton
import com.au.testapp.R
import com.au.testapp.modules.firstModule.fragments.CountryDetailListFragment
import com.au.testapp.modules.firstModule.model.Results
import com.au.testapp.modules.firstModule.viewmodel.CountryDetailViewModel

/**
 * Activity class to show country details list
 */
class CountryDetailActivity : BaseActivity() {
    /* View model for the activity and related fragments to observe the data */
    private lateinit var mViewModel: CountryDetailViewModel

    /* Observers required for the country details list */
    private lateinit var mGetCountryDetailsListObserver: Observer<Results>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionBar()
        setContentView(R.layout.activity_country_detail, null)
        mViewModel = ViewModelProviders.of(this).get(CountryDetailViewModel::class.java)
        showCountryDetailsListFragment()
        initializeObserverForFetchingCountryList()
    }

    override fun onDestroy() {
        super.onDestroy()
        /* We don't want to observe any data change when this fragment is in the
        current method and will be destroyed */
        if (mGetCountryDetailsListObserver != null) {
            mViewModel.fetchCountryList(false).removeObserver(mGetCountryDetailsListObserver)
        }
    }

    private fun initActionBar() {
        /* Initialize action bar Set No title. This will be updated
        later when we have results ready */
        initActionBar(BaseActivity.INVALID_TITLE, BaseActivity.INVALID_HOME_ICON, true)
        showActionBar()
    }

    private fun showCountryDetailsListFragment() {
        val countryDetailsListFragment = CountryDetailListFragment.newInstance()
        showFragment(
            R.id.main_content_view,
            countryDetailsListFragment,
            CountryDetailListFragment.FRAGMENT_TAG,
            false,
            true
        )
    }

    fun fetchCountryDetailsList(forceLoad: Boolean): Boolean {
        // check if the network is available
        if (!isNetworkAvailable()) {
            Toast.makeText(
                this@CountryDetailActivity,
                R.string.internet_not_available_error,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        // fetch the country list from the  saved state or network
        mViewModel.fetchCountryList(forceLoad).observe(this, mGetCountryDetailsListObserver)
        return true
    }

    private fun initializeObserverForFetchingCountryList() {
        // Observe for the changes in the results
        mGetCountryDetailsListObserver = Observer<Results> { results ->
            if (results.title == null) {
                // remove the existing title and display no title
                updateActionBarTitle(null)
            } else {
                // show title
                updateActionBarTitle(results.title)
            }
            // hide progress dialog
            hideProgressDialog()
            // stop Counting Idling Resource
            IdlingResourceSingleton.decrement()
        }
    }

    /* Show progress dialog */
    public override fun showProgressDialog() {
        super.showProgressDialog()
    }
}
