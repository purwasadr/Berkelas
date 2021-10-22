package com.alurwa.common.util

import android.content.Context
import com.alurwa.common.R

enum class AttendanceType(val code: String) {
    PRESENCE("presence") {
        override fun getValue(context: Context) =
            context.getString(R.string.presence)
    },
    PERMIT("permit") {
        override fun getValue(context: Context) =
            context.getString(R.string.permit)
    },
    SICK("sick") {
        override fun getValue(context: Context) =
            context.getString(R.string.sick)
    };

    abstract fun getValue(context: Context): String
}