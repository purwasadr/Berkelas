package com.alurwa.berkelas.ui.attendanceaddedit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityAttendanceAddEditBinding
import com.alurwa.berkelas.extension.MODE_ADD
import com.alurwa.berkelas.extension.MODE_EDIT
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AttendanceAddEditActivity : AppCompatActivity() {
    private val binding: ActivityAttendanceAddEditBinding by lazy(LazyThreadSafetyMode.NONE){
        ActivityAttendanceAddEditBinding.inflate(layoutInflater)
    }

    private val viewModel: AttendanceAddEditViewModel by viewModels()
    private var isDoneVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "Add Attendance", true)

    }

    private fun saveAttendance() {
        if (viewModel.mode == MODE_ADD) {
            addAttendance()
        } else if (viewModel.mode == MODE_EDIT) {

        }
    }

    private fun addAttendance() {
        lifecycleScope.launch {
            viewModel.addAttendance(inputToAddAttendance()).collectLatest { result ->
                handleResult(result)
            }
        }
    }

    private fun handleResult(result: Result<*>) {
        result.onSuccess {
            finish()
        }.onError {
            setMenuDoneVisibility(true)
        }.onLoading {
            setMenuDoneVisibility(false)
        }
    }

    private fun inputToAddAttendance() = Attendance(
        date = viewModel.date.value.toString()
    )

    private fun setMenuDoneVisibility(value: Boolean) {
        isDoneVisible = value
        invalidateOptionsMenu()
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
        menuInflater.inflate(R.menu.menu_done_common, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_done -> {
                saveAttendance()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_DATE = "extra_date"
    }

}