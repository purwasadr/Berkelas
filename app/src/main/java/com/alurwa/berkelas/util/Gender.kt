package com.alurwa.berkelas.util

import android.content.Context
import com.alurwa.berkelas.R

enum class Gender(val code: Int) {
    MALE(1) {
        override fun getValue(context: Context) = context.getString(R.string.male)
    },
    FEMALE(2) {
        override fun getValue(context: Context) = context.getString(R.string.female)
    };

    abstract fun getValue(context: Context): String
}