package com.alurwa.berkelas.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.alurwa.berkelas.R
import com.alurwa.berkelas.util.Gender
import com.alurwa.berkelas.util.Role
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

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

    @JvmStatic
    @BindingAdapter("tvDate")
    fun tvDate(view: TextView, value: Long?) {
        if (value != null) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val date = Date(value)

            view.text = dateFormat.format(date)
        }
    }

    @JvmStatic
    @BindingAdapter("tvRole")
    fun tvRole(view: TextView, value: String?) {
        if (value != null) {
            val roleName = Role.values().find {
                it.code == value
            }
            view.text = roleName?.toString(view.context)
        }
    }

    @BindingAdapter("txtRupiah", "txtRupiahIsPositive", requireAll = false)
    @JvmStatic
    fun txtRupiah(view: TextView, value: Int?, isPositive: Boolean = true) {
        if (value != null) {
            val decimalFormat = DecimalFormat("#,###").format(value)
            val rp = if (isPositive) {
                "Rp. "
            } else {
                "- Rp. "
            }
            view.text = rp + decimalFormat
        }
    }
}