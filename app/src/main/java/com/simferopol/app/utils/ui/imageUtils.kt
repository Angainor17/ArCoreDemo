package com.simferopol.app.utils.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("app:src")
fun loadIcon(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Picasso.get().load(url).into(view)
    }
}
