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
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onSuccess
import com.alurwa.common.util.AttendanceType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        binding.appbar.toolbar.setupToolbar(this, "Detail Presensi", true)

        setupDataBinding()
        setupList()
        setupFab()
        observe()
        observeAttendanceDetailItems()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupList() {
        binding.listAttendanceDetail.layoutManager = LinearLayoutManager(this)
        binding.listAttendanceDetail.adapter = adapter
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            fillAttendance()
        }
    }

    private fun fillAttendance() {
        val arrayItems = Array(AttendanceType.values().size) {
            AttendanceType.values()[it].getValue(this)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih")
            .setItems(arrayItems) { dialog, which ->
                dialog.dismiss()
                setAttendance(AttendanceType.values()[which].code)
            }
            .show()
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
                    adapter.submitList(it.items)
                    viewModel.setAttendanceDetailHeader(it.info)
                }
            }
        }
    }

    private fun setAttendance(code: String) {
        val userId = viewModel.userId ?: return

        lifecycleScope.launch {
            viewModel.setAttendance(code, userId).collectLatest {
                it.onSuccess {

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