package com.alurwa.berkelas.extension

import com.alurwa.berkelas.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun MaterialAlertDialogBuilder.singleChoiceItems(
    items: Array<out CharSequence>,
    selectedItem: Int,
    action: (which: Int) -> Unit
): MaterialAlertDialogBuilder {
    this.setSingleChoiceItems(items, selectedItem) { dialog, which ->
        action(which)
        dialog.dismiss()
    }
    return this
}

fun MaterialAlertDialogBuilder.negativeBtnCancel(): MaterialAlertDialogBuilder {
    this.setNegativeButton(R.string.btn_cancel) { dialog, _ ->
        dialog.dismiss()
    }
    return this
}