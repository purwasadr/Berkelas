package com.alurwa.berkelas.model

import androidx.annotation.DrawableRes

data class AccountMenuItem(
    val name: String,
    @DrawableRes
    val drawableRes: Int,
    val isActive: Boolean
)
