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
import com.au.testapp.R
import com.au.testapp.modules.firstModule.activities.FirstActivity
import com.au.testapp.modules.firstModule.adapters.CountryDetailsListAdapter
import com.au.testapp.modules.firstModule.model.Results
import com.au.testapp.modules.firstModule.model.Row
import com.au.testapp.modules.firstModule.network.ErrorHandler
import com.au.testapp.modules.firstModule.viewmodel.FirstViewModel
import java.util.*

/**
 * Fragment to detail list of the country
 */
class CountryDetailListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val FRAGMENT_TAG = CountryDetailListFragment::class.java.simpleName

        fun newInstance(): CountryDetailListFragment {
            return CountryDetailListFragment()
        }
    }

    private lateinit var mActivity: FirstActivity
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewModel: FirstViewModel
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    /*Observers required*/
    /* This observer is required for the country details list.*/
    private lateinit var mGetCountryDetailsListObserver: Observer<Results>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as FirstActivity
        mViewModel = ViewModelProviders.of(mActivity).get(FirstViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        // We don't want to observe any data change when this fragment is in the
        // current method and will be destroyed
        if (mGetCountryDetailsListObserver != null)
            mViewModel.countryDetailsResults.removeObserver(mGetCountryDetailsListObserver)
    }

    override fun onRefresh() {
        // observe for any update on the server
        mViewModel.updatedResults.observe(mActivity, mGetCountryDetailsListObserver)
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
        // check if the network is available
        if (!mActivity.isNetworkAvailable()) {
            return
        }

        mViewModel.countryDetailsResults.observe(mActivity, mGetCountryDetailsListObserver)
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
            if (mSwipeRefreshLayout.isRefreshing) {
                mSwipeRefreshLayout.isRefreshing = false
            }
        }
    }


    private fun setDataOnList(results: Results?) {
        // create a list of items initially the list is empty. if the list is empty the "Empty view is shown on recycler view"
        val itemList = ArrayList<Row>()
        if (results?.rows != null) {
            itemList.addAll(results?.rows!!)
        }
        // set data on on the list
        var adapter = mRecyclerView.adapter as CountryDetailsListAdapter?
        if (adapter == null) {
            adapter = CountryDetailsListAdapter(itemList)
            mRecyclerView.adapter = adapter
        } else {
            adapter.replaceData(itemList)
        }
    }
}