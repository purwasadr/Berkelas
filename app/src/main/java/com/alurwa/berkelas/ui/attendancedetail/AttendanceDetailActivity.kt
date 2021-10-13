package com.alurwa.berkelas.ui.attendancedetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.adapter.AttendanceDetailAdapter
import com.alurwa.berkelas.databinding.ActivityAttendanceDetailBinding
import com.alurwa.common.model.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AttendanceDetailActivity : AppCompatActivity() {

    private val binding: ActivityAttendanceDetailBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAttendanceDetailBinding.inflate(layoutInflater)
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        AttendanceDetailAdapter()
    }

    private val viewModel: AttendanceDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupList()
        observe()
        observeAttendanceDetailItems()
    }

    private fun setupList() {
        binding.listAttendanceDetail.layoutManager = LinearLayoutManager(this)
        binding.listAttendanceDetail.adapter = adapter
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeUsersAndAttendance.collectLatest { pair ->
                    pair.first.onSuccess {
                        viewModel.setUsers(it)
                    }

                    pair.second.onSuccess {
                        if (it != null) viewModel.setAttendance(it)
                    }
                }
            }
        }
    }

    private fun observeAttendanceDetailItems() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.attendanceDetailItems.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DATE = "extra_date"
    }
}