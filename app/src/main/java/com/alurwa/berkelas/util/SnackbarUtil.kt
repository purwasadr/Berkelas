package com.alurwa.berkelas.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackbarUtil {
    fun showShort(view: View, text: String?) {
        Snackbar.make(view, text.toString(), Snackbar.LENGTH_SHORT).show()
    }
}

