package com.au.testapp.modules.firstModule.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.au.testapp.R
import com.au.testapp.modules.firstModule.adapters.CountryDetailsListAdapter
import com.au.testapp.modules.firstModule.model.Results
import com.au.testapp.modules.firstModule.model.Row
import com.au.testapp.modules.firstModule.viewmodel.FirstViewModel

class CountryDetailListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val FRAGMENT_TAG = CountryDetailListFragment::class.java.simpleName

        fun newInstance(): CountryDetailListFragment {
            return CountryDetailListFragment()
        }
    }

    private lateinit var mActivity: FragmentActivity
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewModel: FirstViewModel
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mResults: Results
    /* This observer is required for the country details list.*/
    private lateinit var mGetCountryDetailsListObserver: Observer<Results>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity!!
        mViewModel = ViewModelProviders.of(mActivity).get(FirstViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_country_detail_list, container, false)
        initViews(view)
        populateCountryDetailsList()
        return view
    }

    override fun onRefresh() {

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
        // Observe for the changes in the results
        mGetCountryDetailsListObserver = Observer { results ->
            if (results != null) {
                // the results are downloaded now. Update the list.
                mResults = results
                setData(mResults.rows)
            }
            if (mSwipeRefreshLayout.isRefreshing) {
                mSwipeRefreshLayout.isRefreshing = false
            }
        }

        mViewModel.countryDetailsResults.observe(mActivity, mGetCountryDetailsListObserver)
    }

    private fun setData(rows: List<Row>?) {
        var adapter = mRecyclerView.adapter as CountryDetailsListAdapter?
        if (adapter == null) {
            val array = arrayListOf<Row>()
            array.addAll(rows!!)
            adapter = CountryDetailsListAdapter(array)
            mRecyclerView.adapter = adapter
        } else {
            adapter.replaceData(rows)
        }
    }
}
