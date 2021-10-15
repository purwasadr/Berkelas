package com.alurwa.berkelas.extension

import android.content.Intent

const val EXTRA_MODE = "extra_mode"
const val MODE_ADD = 0
const val MODE_EDIT = 1

fun Intent.putExtraModeAdd() = this.putExtra(EXTRA_MODE, MODE_ADD)

fun Intent.putExtraModeEdit() = this.putExtra(EXTRA_MODE, MODE_EDIT)