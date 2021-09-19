package com.alurwa.berkelas.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

fun Toolbar.setupToolbar(
    appCompat: AppCompatActivity,
    title: String,
    isHomeAsUpEnable: Boolean = false
) {
    with(appCompat) {
        setSupportActionBar(this@setupToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(isHomeAsUpEnable)
        supportActionBar?.setTitle(title)
    }

}