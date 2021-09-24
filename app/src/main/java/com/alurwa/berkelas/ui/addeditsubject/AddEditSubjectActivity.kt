package com.alurwa.berkelas.ui.addeditsubject

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityAddEditSubjectBinding
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onError
import com.alurwa.common.model.onSuccess
import com.alurwa.common.util.autoId
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddEditSubjectActivity : AppCompatActivity() {
    private val binding: ActivityAddEditSubjectBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAddEditSubjectBinding.inflate(layoutInflater)
    }

    private val viewModel: AddEditSubjectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setupToolbar(
            this,
            "Add Subject",
            true
        )

        setupInputView()
    }

    private fun setupInputView() {
        with(binding) {
            actStartTime.setOnClickListener {
                doChangeStartTime()
            }
            actEndTime.setOnClickListener {
                doChangeEndTime()
            }

            lifecycleScope.launch {
                viewModel.day.collectLatest {
                    val dayOfWeek = resources.getStringArray(R.array.day_of_week)

                    actDay.setText(dayOfWeek[it])
                }
            }
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
        picker.show(supportFragmentManager, "time_picker")

        picker.addOnPositiveButtonClickListener {
            binding.actStartTime.setText("${picker.hour}:${picker.minute}")
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
        picker.show(supportFragmentManager, "time_picker")

        picker.addOnPositiveButtonClickListener {
            binding.actEndTime.setText("${picker.hour}:${picker.minute}")
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
    }

    private fun saveSubject() {
        val mode = viewModel.mode
        val day = viewModel.day.value

        if (checkValidity()) {
            if (mode == MODE_ADD) {
                lifecycleScope.launch {
                    viewModel.addSubject(day, inputToAddObject())
                        .collectLatest {
                            it.onSuccess {
                                finish()
                            }.onError {

                            }
                        }
                }
            } else if (mode == MODE_EDIT) {
                lifecycleScope.launch {
                    viewModel.editSubject(day, inputToEditObject())
                        .collectLatest {
                            it.onSuccess {
                                finish()
                            }.onError {

                            }
                        }
                }
            }
        }
    }

    private fun checkValidity(): Boolean {
        var isValid = true
        with(binding) {
            if (edtSubject.text.toString().isEmpty()) {
                isValid = false
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
        viewModel.subjectItem?.copy(
            id = autoId(),
            subject = binding.edtSubject.text.toString(),
            startTime = binding.actStartTime.text.toString(),
            endTime = binding.actEndTime.text.toString(),
            teacher = binding.edtTeacher.text.toString()

        )!!

    private fun inputToEditObject() =
        viewModel.subjectItem?.copy(
            subject = binding.edtSubject.text.toString(),
            startTime = binding.actStartTime.text.toString(),
            endTime = binding.actEndTime.text.toString(),
            teacher = binding.edtTeacher.text.toString()

        )!!

    override fun onSupportNavigateUp(): Boolean {
        finish()
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