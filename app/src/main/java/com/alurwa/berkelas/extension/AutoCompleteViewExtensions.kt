package com.alurwa.berkelas.extension

import android.content.Context
import android.view.View
import android.widget.AutoCompleteTextView
import com.alurwa.berkelas.util.KeyboardUtil

fun AutoCompleteTextView.setOnClickForDialog(
    context: Context,
    onClickListener: (view: View) -> Unit
) {
    this.setOnFocusChangeListener { v, hasFocus ->
        if (v.isInTouchMode && hasFocus) v.performClick()
    }

    this.setOnClickListener {
        KeyboardUtil.hideKeyboard(context, it)
        onClickListener.invoke(it)
    }
}
