package com.alurwa.berkelas.ui.addeditsubject

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityAddEditSubjectBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.setOnClickForDialog
import com.alurwa.berkelas.extension.showError
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.SubjectItem
import com.alurwa.common.model.onError
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onSuccess
import com.alurwa.common.util.autoId
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.*

@AndroidEntryPoint
class AddEditSubjectActivity : AppCompatActivity() {
    private val binding: ActivityAddEditSubjectBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAddEditSubjectBinding.inflate(layoutInflater)
    }

    private val viewModel: AddEditSubjectViewModel by viewModels()

    private var isDoneVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupDataBinding()
        setupInputView()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupInputView() {
        with(binding) {
            actStartTime.setOnClickForDialog(this@AddEditSubjectActivity) {
                doChangeStartTime()
            }

            actEndTime.setOnClickForDialog(this@AddEditSubjectActivity) {
                doChangeEndTime()
            }

            actDay.setOnClickForDialog(this@AddEditSubjectActivity) {
                doChangeDay()
            }
        }

        if (viewModel.mode == MODE_EDIT) {
            binding.actDay.isEnabled = false
        }
    }

    private fun doChangeStartTime() {
        val cal = Calendar.getInstance()

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val _minute = cal.get(Calendar.MINUTE)

        val picker = MaterialTimePicker.Builder()
            .setHour(hour)
            .setMinute(_minute)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Pilih Waktu Mulai")
            .build()
        picker.show(supportFragmentManager, "time_picker_start_time")

        picker.addOnPositiveButtonClickListener {
            val minute = DecimalFormat("00").format(picker.minute)

            binding.actStartTime.setText("${picker.hour}:${minute}")
        }
    }

    private fun doChangeEndTime() {
        val cal = Calendar.getInstance()

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val _minute = cal.get(Calendar.MINUTE)

        val picker = MaterialTimePicker.Builder()
            .setHour(hour)
            .setMinute(_minute)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Pilih Waktu Mulai")
            .build()
        picker.show(supportFragmentManager, "time_picker_end_time")

        picker.addOnPositiveButtonClickListener {
            val minute = DecimalFormat("00").format(picker.minute)

            binding.actEndTime.setText("${picker.hour}:${minute}")
        }
    }

    private fun doChangeDay() {
        val dayOfWeek = resources.getStringArray(R.array.day_of_week)
        val day = viewModel.day.value

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih hari")
            .setSingleChoiceItems(dayOfWeek, day) { dialog, which ->
                viewModel.setDay(which)
                dialog.dismiss()
            }
            .show()
    }

    private fun saveSubject() {
        val mode = viewModel.mode

        if (checkValidity()) {
            if (mode == MODE_ADD) {
                addSubject()
            } else if (mode == MODE_EDIT) {
                editSubject()
            }
        }
    }

    private fun addSubject() {
        val day = viewModel.day.value
        lifecycleScope.launch {
            viewModel.addSubject(day, inputToAddObject())
                .collectLatest {
                    it.onSuccess {
                        finish()
                    }.onError {
                        setMenuDoneVisibility(true)
                    }.onLoading {
                        setMenuDoneVisibility(false)
                    }
                }
        }
    }

    private fun editSubject() {
        val day = viewModel.day.value
        lifecycleScope.launch {
            viewModel.editSubject(day, inputToEditObject())
                .collectLatest {
                    it.onSuccess {
                        finish()
                    }.onError {
                        setMenuDoneVisibility(true)
                    }.onLoading {
                        setMenuDoneVisibility(false)
                    }
                }
        }
    }

    private fun setMenuDoneVisibility(value: Boolean) {
        isDoneVisible = value
        invalidateOptionsMenu()
    }

    private fun checkValidity(): Boolean {
        var isValid = true
        with(binding) {
            if (edtSubject.text.toString().isEmpty()) {
                isValid = false
                tilSubject.showError(getString(R.string.input_error_subject))
            } else {
                tilSubject.removeError()
            }

            if (edtTeacher.text.toString().isEmpty()) {
                isValid = false
            }

            if (actStartTime.text.toString().isEmpty()) {
                isValid = false
            }
            if (actEndTime.text.toString().isEmpty()) {
                isValid = false
            }
        }

        return isValid
    }

    private fun inputToAddObject() =
        SubjectItem(
            id = autoId(),
            subject = binding.edtSubject.text.toString(),
            startTime = binding.actStartTime.text.toString(),
            endTime = binding.actEndTime.text.toString(),
            teacher = binding.edtTeacher.text.toString()

        )

    private fun inputToEditObject() =
        viewModel.subjectItem?.copy(
            subject = binding.edtSubject.text.toString(),
            startTime = binding.actStartTime.text.toString(),
            endTime = binding.actEndTime.text.toString(),
            teacher = binding.edtTeacher.text.toString()

        )!!

    private fun setupToolbar() {
        val title = if (viewModel.mode == MODE_ADD) {
            "Add Subject"
        } else if (viewModel.mode == MODE_EDIT) {
            "Edit Subject"
        } else {
            throw IllegalStateException()
        }

        binding.appbar.toolbar.setupToolbar(
            this,
            title,
            true
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_done)?.isVisible = isDoneVisible
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_edit_subject, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {
                saveSubject()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        const val EXTRA_MODE = "extra_mode"
        const val EXTRA_SUBJECT = "extra_subject"
        const val MODE_ADD = 0
        const val MODE_EDIT = 1
        const val EXTRA_DAY = "extra_day"
    }
}