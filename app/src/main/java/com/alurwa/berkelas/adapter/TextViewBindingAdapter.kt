package com.alurwa.berkelas.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.alurwa.berkelas.R
import com.alurwa.berkelas.util.Gender

object TextViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("tvDayOfWeek")
    fun tvDayOfWeek(view: TextView, value: Int?) {
        val dayOfWeek = view
            .resources
            .getStringArray(R.array.day_of_week)
            .getOrNull(value ?: -1)
        view.text = dayOfWeek
    }

    @JvmStatic
    @BindingAdapter("txtGender")
    fun txtGender(view: TextView, code: Int) {
        val gender = Gender.values().getOrNull(code - 1)?.getValue(view.context) ?: ""

        view.text = gender
    }
}