package com.alurwa.berkelas.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Purwa Shadr Al 'urwa on 22/07/2021
 */

object KeyboardUtil {
    /**
     * Untuk menyembunyikan keyboard
     */
    fun hideKeyboard(context: Context, focus: View?) {

        if (focus != null) {
            (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(focus.windowToken, 0)
        }
    }
}