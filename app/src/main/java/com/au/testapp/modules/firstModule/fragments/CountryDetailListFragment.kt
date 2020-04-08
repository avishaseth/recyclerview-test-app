package com.au.testapp.modules.firstModule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.au.testapp.IdlingResourceSingleton
import com.au.testapp.R
import com.au.testapp.modules.firstModule.adapters.CountryDetailsListAdapter
import com.au.testapp.modules.firstModule.model.Results
import com.au.testapp.modules.firstModule.model.Row
import com.au.testapp.modules.firstModule.network.ErrorHandler
import com.au.testapp.modules.firstModule.activities.CountryDetailActivity
import com.au.testapp.modules.firstModule.viewmodel.CountryDetailViewModel
import java.util.*

/**
 * Fragment to show detail list of the country
 */
class CountryDetailListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val FRAGMENT_TAG = CountryDetailListFragment::class.java.simpleName

        fun newInstance(): CountryDetailListFragment {
            return CountryDetailListFragment()
        }
    }

    private lateinit var mActivity: CountryDetailActivity
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewModel: CountryDetailViewModel
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    /* This observer is for the country details list result.*/
    private lateinit var mGetCountryDetailsListObserver: Observer<Results>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as CountryDetailActivity
        mViewModel = ViewModelProviders.of(mActivity).get(CountryDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_country_detail_list, container, false)
        initViews(view)
        // set the empty list on the list in the beginning until the data is downloaded.
        setDataOnList(null)
        initializeObserverForFetchingCountryList();
        populateCountryDetailsList()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /* We don't want to observe any data change when this fragment is in the
         current method and will be destroyed */
        if (mGetCountryDetailsListObserver != null) {
            mViewModel.fetchCountryList(false).removeObserver(mGetCountryDetailsListObserver)
        }
    }

    override fun onRefresh() {
        // observe for any update on the server
        // observe the changes on activity too and do appropriate action when results are fetched.
        // This will return false only when the network state returns false
        var isSuccess = mActivity.fetchCountryDetailsList(true)
        // only if the operation is successful on activity, do the same for the activity
        if (isSuccess)
            mViewModel.fetchCountryList(true).observe(this, mGetCountryDetailsListObserver)
        else
        // dismiss the swipe refresh layout as we are not able to fetch the results mainly due to internet in-availability
            mSwipeRefreshLayout.isRefreshing = false
    }

    private fun initViews(view: View) {
        // set swipe refresh layout
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        // set recycler view
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mRecyclerView.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(mActivity)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun populateCountryDetailsList() {
        // observe the changes on activity too and do appropriate action when results are fetched.
        var isSuccess = mActivity.fetchCountryDetailsList(false)
        // only if the operation is successful on activity, do the same for the fragment.
        if (isSuccess) {
            //start Counting Idling Resource
            IdlingResourceSingleton.increment()
            mActivity.showProgressDialog()
            mViewModel.fetchCountryList(false).observe(this, mGetCountryDetailsListObserver)
        }
    }

    private fun initializeObserverForFetchingCountryList() {
        // Observe for the changes in the results
        mGetCountryDetailsListObserver = Observer { results ->
            // the results are downloaded now. Update the list.
            if (results.rows == null && results.isApiSuccess == false) {
                // check if the error scenario
                ErrorHandler.instance
                    .handleError(mActivity, results.resultCode)
            }
            setDataOnList(results)
            mSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setDataOnList(results: Results?) {
        // list of items initialised as empty. if the list is empty the "Empty view" is shown on recyclerview
        val itemList = ArrayList<Row>()
        if (results?.rows != null) {
            itemList.addAll(results?.rows!!)
        }
        // set data on the list
        var adapter = mRecyclerView.adapter as CountryDetailsListAdapter?
        if (adapter == null) {
            adapter = CountryDetailsListAdapter(itemList)
            mRecyclerView.adapter = adapter
        } else {
            adapter.replaceData(itemList)
        }
    }
}
