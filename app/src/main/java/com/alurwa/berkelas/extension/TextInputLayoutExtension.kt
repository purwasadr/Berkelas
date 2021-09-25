package com.alurwa.berkelas.extension

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError(text: String) {
    this.isErrorEnabled = true
    this.error = text
}

fun TextInputLayout.removeError() {
    this.isErrorEnabled = false
    this.error = ""
}