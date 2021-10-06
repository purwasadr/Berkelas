package com.alurwa.berkelas.util

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

object SnackbarUtil {
    fun showShort(view: View, text: String?) {
        Snackbar.make(view, text.toString(), Snackbar.LENGTH_SHORT).show()
    }

    fun showShort(view: View, @StringRes stringRes: Int) {
        Snackbar.make(view, view.context.getString(stringRes), Snackbar.LENGTH_SHORT).show()
    }
}

