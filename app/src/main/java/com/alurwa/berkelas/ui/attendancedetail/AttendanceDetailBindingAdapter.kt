package com.alurwa.berkelas.ui.attendancedetail

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.alurwa.berkelas.R
import com.alurwa.berkelas.util.AttendanceCheck

object AttendanceDetailBindingAdapter {
    @JvmStatic
    @BindingAdapter("imgAttendanceCheck")
    fun imgAttendanceCheck(view: ImageView, value: String) {
        val color: Int? = when (value) {
            AttendanceCheck.PRESENCE.code -> {
                R.color.green_500

            }

            AttendanceCheck.PERMIT.code -> {
                R.color.blue_500
            }

            AttendanceCheck.SICK.code -> {
                R.color.red_500
            }
            else -> {
                null
            }
        }

        if (color != null) {
            view.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(view.context, color)
            )
            view.isVisible = true
        } else {
            view.isVisible = false
        }
    }
}