package com.au.testapp.modules.firstModule.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.au.testapp.BaseActivity
import com.au.testapp.R
import com.au.testapp.modules.firstModule.fragments.CountryDetailListFragment
import com.au.testapp.modules.firstModule.viewmodel.MainViewModel

/**
 * Activity class to show country details list
 */
class FirstActivity : BaseActivity() {
    /* View model for the activity and related fragments to observe the data */
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionBar()
        setContentView(R.layout.activity_first, null)
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        showCountryDetailsListFragment()

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

}
