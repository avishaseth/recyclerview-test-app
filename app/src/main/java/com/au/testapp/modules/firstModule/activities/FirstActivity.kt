package com.au.testapp.modules.firstModule.activities

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.au.testapp.BaseActivity
import com.au.testapp.R
import com.au.testapp.modules.firstModule.fragments.CountryDetailListFragment
import com.au.testapp.modules.firstModule.model.Results
import com.au.testapp.modules.firstModule.viewmodel.FirstViewModel

/**
 * Activity class to show country details list
 */
class FirstActivity : BaseActivity() {
    /* View model for the activity and related fragments to observe the data */
    private lateinit var mViewModel: FirstViewModel
    /* Observers required for the country details list */
    private lateinit var mGetCountryDetailsListObserver: Observer<Results>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionBar()
        setContentView(R.layout.activity_first, null)
        mViewModel = ViewModelProviders.of(this).get(FirstViewModel::class.java)
        showCountryDetailsListFragment()
        fetchCountryDetailsList()
    }

    private fun initActionBar() {
        /* Initialize action bar Set No title. This will be updated
        later when we have results ready */
        initActionBar(BaseActivity.INVALID_TITLE, BaseActivity.INVALID_HOME_ICON, true)
        showActionBar()
    }

    private fun showCountryDetailsListFragment() {
        val countryDetailsListFragment = CountryDetailListFragment.newInstance()
        showFragment(R.id.main_content_view, countryDetailsListFragment, CountryDetailListFragment.FRAGMENT_TAG, false, true)
    }

    /* Update title bar with value from API */
    private fun fetchCountryDetailsList() {
        // fetch the country list from the saved state or network
        // TODO show progress dialog before fetching the results.
        mGetCountryDetailsListObserver = Observer<Results> { results ->
            if (results == null) {
                // show the message that the request is failure.
                Toast.makeText(this@FirstActivity, R.string.network_error, Toast.LENGTH_SHORT).show()
            } else {
                // Now update the action bar title.
                updateActionBarTitle(results.title ?: getString(R.string.empty_title))
            }
        }
        mViewModel.countryDetailsResults.observe(this, mGetCountryDetailsListObserver)
    }

}
