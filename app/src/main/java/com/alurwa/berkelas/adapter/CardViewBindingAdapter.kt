package com.alurwa.berkelas.adapter

import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.alurwa.berkelas.util.CardColor

object CardViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("cvBackColor")
    fun cvBackColor(view: CardView, code: String?) {
        if (code != null) {
            val color = CardColor.valueOf(code.uppercase()).getColorRes(view.context)

            view.setBackgroundColor(color)
        }
    }
}