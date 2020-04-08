package com.au.testapp.modules.firstModule.databinding

import android.widget.ImageView
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.au.testapp.modules.firstModule.model.Row
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/* Data binding class for the Country Detail List Item */
class CountryDetailItemViewBinding(row: Row) : BaseObservable() {
    var mImageUrl: String? = null
    private var mTitle: String? = null
    private var mDescription: String? = null

    init {
        mTitle = row.title
        mDescription = row.description
        mImageUrl = row.imageHref
    }

    val title: CharSequence?
        get() = mTitle

    val description: CharSequence
        get() = mDescription ?: "No description available"

    /* load the video thumbnail */
    companion object {
        @JvmStatic
        @BindingAdapter("profileImage")
        fun loadImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.context)
                    .load(imageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(view)
        }
    }
}