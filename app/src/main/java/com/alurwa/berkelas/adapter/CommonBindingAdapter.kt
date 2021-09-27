package com.alurwa.berkelas.adapter

import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import com.alurwa.berkelas.util.Gender
import java.text.SimpleDateFormat
import java.util.*

object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter("actGender")
    fun actGender(view: AutoCompleteTextView, code: Int) {
        val gender = Gender.values().getOrNull(code - 1)?.getValue(view.context) ?: ""

        view.setText(gender)
    }

    @JvmStatic
    @BindingAdapter("actDateOfBirth")
    fun actDateOfBirth(view: AutoCompleteTextView, value: Long?) {
        if (value != null) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val date = Date(value)

            view.setText(dateFormat.format(date))
        }
    }
}