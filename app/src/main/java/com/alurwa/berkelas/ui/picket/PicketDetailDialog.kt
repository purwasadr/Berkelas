package com.alurwa.berkelas.ui.picket

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.DialogPicketDetailBinding
import com.alurwa.berkelas.model.PicketUi
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PicketDetailDialog(
    context: Context,
    val picketUi: PicketUi
) : MaterialAlertDialogBuilder(context) {

    private var builder: AlertDialog? = null

    private var binding = DialogPicketDetailBinding.inflate(LayoutInflater.from(context))

    init {

        setView(binding.root)

        setPositiveButton(R.string.title_close) { dialog, _ ->
            dialog.dismiss()
        }

        setupView()
    }

    private fun setupView() {
        binding.picketUi = picketUi
//        binding.tvSubject.text = subjectItem.subject
//        binding.tvTime.text = "${subjectItem.startTime} - ${subjectItem.endTime}"
//        binding.tvTeacher.text = if (subjectItem.teacher.isNotEmpty()) {
//            subjectItem.teacher
//        } else {
//            "-"
//        }
    }

    fun setOnClickBtnEdit(listener: () -> Unit): PicketDetailDialog {
        binding.btnEdit.setOnClickListener {
            listener.invoke()

            builder?.dismiss()
        }

        return this
    }

    fun setOnClickBtnDelete(listener: () -> Unit): PicketDetailDialog {
        binding.btnDelete.setOnClickListener {
            listener.invoke()
            builder?.dismiss()
        }

        return this
    }

    override fun show(): AlertDialog {

        // Mengambil properti super.show agar mendapatkan dismiss()
        builder = super.show()
        return builder!!
    }
}