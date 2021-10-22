package com.alurwa.berkelas.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.alurwa.berkelas.R

enum class CardColor(val code: String) {
    RED("red") {
        override fun getColorRes(context: Context) =
            ContextCompat.getColor(context, R.color.red_600)

        override fun getName(context: Context) = context.getString(R.string.red)
    },
    PURPLE("purple") {
        override fun getColorRes(context: Context) =
            ContextCompat.getColor(context, R.color.purple_600)

        override fun getName(context: Context) = context.getString(R.string.purple)
    },
    BLUE("blue") {
        override fun getColorRes(context: Context) =
            ContextCompat.getColor(context, R.color.blue_600)

        override fun getName(context: Context) = context.getString(R.string.blue)
    },
    GREEN("green") {
        override fun getColorRes(context: Context) =
            ContextCompat.getColor(context, R.color.green_600)

        override fun getName(context: Context) = context.getString(R.string.green)
    },
    BROWN("brown") {
        override fun getColorRes(context: Context) =
            ContextCompat.getColor(context, R.color.brown_600)

        override fun getName(context: Context) = context.getString(R.string.brown)
    },
    GRAY("gray") {
        override fun getColorRes(context: Context) =
            ContextCompat.getColor(context, R.color.gray_600)

        override fun getName(context: Context) = context.getString(R.string.gray)
    };

    abstract fun getColorRes(context: Context): Int

    abstract fun getName(context: Context): String
}