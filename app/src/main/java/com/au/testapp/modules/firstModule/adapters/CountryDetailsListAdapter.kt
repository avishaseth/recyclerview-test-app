package com.au.testapp.modules.firstModule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.au.testapp.R
import com.au.testapp.databinding.LayoutCountryDetailItemBinding
import com.au.testapp.modules.firstModule.databinding.CountryDetailItemViewBinding
import com.au.testapp.modules.firstModule.model.Row

/**
 * Adapter class to bind the data to the Recycler view.
 */
class CountryDetailsListAdapter(private val mCountryDetailsList: ArrayList<Row>) : RecyclerView.Adapter<CountryDetailsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val binding = DataBindingUtil.inflate<LayoutCountryDetailItemBinding>(LayoutInflater.from(parent.context),
                R.layout.layout_country_detail_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unbind()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val result = mCountryDetailsList[position]
        viewHolder.bind(CountryDetailItemViewBinding(result))
    }

    /* Replace the data on the list */
    fun replaceData(row: List<Row>?) {
        mCountryDetailsList.clear()
        mCountryDetailsList.addAll(row!!)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mCountryDetailsList.size
    }

    class ViewHolder internal constructor(var binding: LayoutCountryDetailItemBinding?) : RecyclerView.ViewHolder(binding!!.getRoot()) {

        internal fun bind(model: CountryDetailItemViewBinding) {
            binding!!.viewmodel = model
            binding!!.executePendingBindings()
        }

        internal fun unbind() {
            binding!!.unbind()
        }
    }
}
