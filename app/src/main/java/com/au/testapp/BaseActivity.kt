package com.au.testapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

/*
* This is the Base Activity to be extended by all activities.
* This holds common functions to be used across application.
 */
open class BaseActivity : AppCompatActivity() {

    companion object {
        var INVALID_HOME_ICON = -1
        var INVALID_TITLE = -1
    }

    private lateinit var mProgressBarLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        mProgressBarLayout = findViewById(R.id.loading)
        setSupportActionBar(toolbar)
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val count = fragmentManager.backStackEntryCount
        if (count >= 1) {
            fragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    /* Set the content View */
    protected fun setContentView(layoutResID: Int, parent: ViewGroup?) {
        val vi =
            applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = vi.inflate(layoutResID, parent)
        val layout = findViewById<View>(R.id.content_view) as FrameLayout
        layout.addView(contentView)
    }

    /* Initialize the action bar */
    protected fun initActionBar(titleResId: Int, homeIcon: Int, displayHomeIcon: Boolean) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            val bgDrawable = resources.getDrawable(R.drawable.actionbar_background, this.theme)
            actionBar.setBackgroundDrawable(bgDrawable)
            if (titleResId == INVALID_TITLE) {
                actionBar.setTitle(R.string.empty_title)
            } else {
                actionBar.setTitle(titleResId)
            }

            if (displayHomeIcon && homeIcon != INVALID_HOME_ICON) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setHomeAsUpIndicator(homeIcon)
            } else {
                actionBar.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    /* Update the title on action bar */
    protected fun updateActionBarTitle(titleResId: String?) {
        val actionBar = supportActionBar
        actionBar!!.title = titleResId ?: getString(R.string.empty_title)

    }


    /* Show Action bar */
    protected fun showActionBar() {
        supportActionBar!!.show()
    }

    /* Show fragment */
    protected fun showFragment(
        fragmentContainerId: Int, fragment: Fragment, fragmentTag: String,
        addToBackStack: Boolean, withAnimation: Boolean
    ) {
        showFragment(
            fragmentContainerId,
            fragment,
            fragmentTag,
            addToBackStack,
            withAnimation,
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_left_in,
            R.anim.slide_out_right
        )
    }

    private fun showFragment(
        fragmentContainerId: Int, fragment: Fragment, fragmentTag: String, addToBackStack: Boolean,
        withAnimation: Boolean, animEnter: Int, animExit: Int, animPopEnter: Int, animPopExit: Int) {
        val previous = supportFragmentManager.findFragmentByTag(fragmentTag)
        var ft = supportFragmentManager.beginTransaction()

        if (previous != null) {
            ft = previous.activity!!.supportFragmentManager.beginTransaction()
        }
        if (withAnimation) {
            ft.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit)
        }
        ft.replace(fragmentContainerId, fragment, fragmentTag)
        if (addToBackStack) {
            ft.addToBackStack(null)
        }
        ft.commit()
    }

    /* Show Progress Dialog */
    protected open fun showProgressDialog() {
        mProgressBarLayout.visibility = View.VISIBLE
    }

    /* Hide Progress Dialog */
    protected fun hideProgressDialog() {
        mProgressBarLayout.visibility = View.GONE
    }

    /* Check if network is available */
    fun isNetworkAvailable(): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = getSystemService(service) as ConnectivityManager?
        val actNw = manager?.getNetworkCapabilities(manager.activeNetwork) ?: return false

        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
