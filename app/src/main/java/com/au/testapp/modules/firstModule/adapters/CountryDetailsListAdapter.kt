package com.au.testapp.modules.firstModule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.au.testapp.R
import com.au.testapp.databinding.LayoutCountryDetailItemBinding
import com.au.testapp.modules.firstModule.databinding.CountryDetailItemViewBinding
import com.au.testapp.modules.firstModule.model.Row
import java.util.*


/**
 * Adapter class to bind the data to the Recycler view.
 */
class CountryDetailsListAdapter(private val mCountryDetailsList: ArrayList<Row>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0
        private const val VIEW_TYPE_COUNTRY_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EMPTY_LIST_PLACEHOLDER -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_item_empty_view,
                    parent, false
                );
                return EmptyViewHolder(view);
            }
            else -> {
                val binding = DataBindingUtil.inflate<LayoutCountryDetailItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_country_detail_item, parent, false
                )
                return CountryItemViewHolder(binding)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is CountryItemViewHolder) {
            holder.unbind();
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is CountryItemViewHolder) {
            val result = mCountryDetailsList[position]
            viewHolder.bind(CountryDetailItemViewBinding(result))
        }
    }

    /* Replace the data on the list */
    fun replaceData(row: List<Row>?) {
        mCountryDetailsList.clear()
        mCountryDetailsList.addAll(row!!)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mCountryDetailsList.let {
            if (it.isEmpty())
                1
            else
                it.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mCountryDetailsList.let {
            if (it.isEmpty())
                VIEW_TYPE_EMPTY_LIST_PLACEHOLDER
            else
                VIEW_TYPE_COUNTRY_ITEM
        }
    }

    internal class EmptyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

    class CountryItemViewHolder internal constructor(var binding: LayoutCountryDetailItemBinding?) :
        RecyclerView.ViewHolder(binding!!.getRoot()) {

        internal fun bind(model: CountryDetailItemViewBinding) {
            binding!!.viewmodel = model
            binding!!.executePendingBindings()
        }

        internal fun unbind() {
            binding!!.unbind()
        }
    }
}