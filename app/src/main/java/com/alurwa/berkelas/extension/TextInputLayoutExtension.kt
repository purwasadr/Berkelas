package com.alurwa.berkelas.extension

import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError(text: String) {
    this.isErrorEnabled = true
    this.error = text
}


fun TextInputLayout.showError(@StringRes resId: Int) {
    this.isErrorEnabled = true
    this.error = this.context.getString(resId)
}

fun TextInputLayout.removeError() {
    this.isErrorEnabled = false
    this.error = ""
}