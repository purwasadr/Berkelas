package com.alurwa.berkelas.ui.attendance

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.adapter.AttendanceAdapter
import com.alurwa.berkelas.databinding.ActivityAttendanceBinding
import com.alurwa.berkelas.extension.putExtraModeAdd
import com.alurwa.berkelas.ui.attendanceaddedit.AttendanceAddEditActivity
import com.alurwa.berkelas.ui.attendancedetail.AttendanceDetailActivity
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AttendanceActivity : AppCompatActivity() {

    private val binding: ActivityAttendanceBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAttendanceBinding.inflate(layoutInflater)
    }

    private val adapter: AttendanceAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AttendanceAdapter() {
            navigateToDetail(it.date)
        }
    }

    private val viewModel: AttendanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "Attendance", true)

        setupFab()
        setupList()
        observe()
    }

    private fun setupList() {
        binding.listAttendance.setHasFixedSize(true)
        binding.listAttendance.layoutManager = LinearLayoutManager(this)
        binding.listAttendance.adapter = adapter
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            navigateToAddAttendance()
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeAttendanceAndUserList.collectLatest { pair ->
                    pair.first.onSuccess {
                        viewModel.setAttendanceList(it)
                    }

                    pair.second.onSuccess {
                        viewModel.setUsers(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeAttendanceItems.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun navigateToDetail(date: Long) {
        Intent(this, AttendanceDetailActivity::class.java)
            .putExtra(AttendanceDetailActivity.EXTRA_DATE, date)
            .also {
                startActivity(it)
            }
    }

    private fun navigateToAddAttendance() {
        Intent(this, AttendanceAddEditActivity::class.java)
            .putExtraModeAdd()
            .also {
                startActivity(it)
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}