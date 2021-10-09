package com.alurwa.berkelas.adapter

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.alurwa.berkelas.R
import com.alurwa.berkelas.util.Gender
import com.alurwa.berkelas.util.Role
import com.bumptech.glide.Glide
import java.text.DecimalFormat
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

    @JvmStatic
    @BindingAdapter("loadImageUrl")
    fun loadImageUrl(view: ImageView, url: String?) {
        Glide.with(view.context)
            .load(url)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("imgProfileUrl")
    fun imgProfileImgUrl(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.setImageResource(R.drawable.ic_round_account_circle_24)
        } else {
            Glide.with(view.context)
                .load(url)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("actDay")
    fun actDay(view: AutoCompleteTextView, day: Int) {
        val dayString = view.context.resources.getStringArray(R.array.day_of_week)

        view.setText(dayString.getOrNull(day))
    }

    @JvmStatic
    @BindingAdapter("setIsVisible")
    fun setIsVisible(view: View, value: Boolean?) {
        if (value != null) {
            view.isVisible = value
        }
    }

    @JvmStatic
    @BindingAdapter("txtRupiah", "txtRupiahIsPositive", requireAll = false)
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

    @JvmStatic
    @BindingAdapter("txtRole")
    fun txtRole(view: TextView, value: String?) {
        if (value != null) {
            val roleName = Role.values().find {
                it.code == value
            }
            view.text = roleName?.toString(view.context)
        }
    }

    /**
     * Makes the View [View.INVISIBLE] unless the condition is met.
     */
    @BindingAdapter("invisibleUnless")
    @JvmStatic
    fun invisibleUnless(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    /**
     * Makes the View [View.GONE] unless the condition is met.
     */
    @BindingAdapter("goneUnless")
    @JvmStatic
    fun goneUnless(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}