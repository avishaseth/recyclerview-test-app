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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.au.testapp.R
import com.au.testapp.modules.firstModule.viewmodel.MainViewModel

class CountryDetailListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val FRAGMENT_TAG = CountryDetailListFragment::class.java.simpleName

        fun newInstance(): CountryDetailListFragment {
            return CountryDetailListFragment()
        }
    }

    private lateinit var mActivity: FragmentActivity
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewModel: MainViewModel
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity!!
        mViewModel = ViewModelProviders.of(mActivity).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_country_detail_list, container, false)
        initViews(view)
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
}
