package com.alurwa.berkelas.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object ImageViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("imgResource")
    fun imgResource(view: ImageView, resId: Int?) {
        if (resId != null) {
            view.setImageResource(resId)
        }
    }
}