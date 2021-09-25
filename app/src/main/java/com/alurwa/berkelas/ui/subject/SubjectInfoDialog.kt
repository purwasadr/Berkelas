package com.alurwa.berkelas.ui.subject

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.alurwa.berkelas.databinding.DialogInfoSubjectBinding
import com.alurwa.common.model.SubjectItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SubjectInfoDialog(
    context: Context,
    val subjectItem: SubjectItem
) : MaterialAlertDialogBuilder(context) {

    private var builder: AlertDialog? = null

    private var binding: DialogInfoSubjectBinding =
        DialogInfoSubjectBinding.inflate(LayoutInflater.from(context))

    init {
        setTitle("Info")

        setView(binding.root)

        setPositiveButton("Tutup") { dialog, _ ->
            dialog.dismiss()
        }

        setupView()
    }

    private fun setupView() {

        binding.txtSubject.text = subjectItem.subject
        binding.txtTime.text = "${subjectItem.startTime} - ${subjectItem.endTime}"
        binding.txtTeacher.text = if (subjectItem.teacher.isNotEmpty()) {
            subjectItem.teacher
        } else {
            "-"
        }
    }

    fun setOnClickBtnEdit(listener: () -> Unit): SubjectInfoDialog {
        binding.btnEdit.setOnClickListener {
            listener.invoke()

            builder?.dismiss()
        }

        return this
    }

    fun setOnClickBtnDelete(listener: () -> Unit): SubjectInfoDialog {
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