package com.alurwa.berkelas.ui.picket

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alurwa.berkelas.R
import com.alurwa.berkelas.adapter.PicketDayAdapter
import com.alurwa.berkelas.databinding.ActivityPicketBinding
import com.alurwa.berkelas.model.PicketDayUi
import com.alurwa.berkelas.model.PicketUi
import com.alurwa.berkelas.ui.picketaddedit.PicketAddEditActivity
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PicketActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPicketBinding.inflate(layoutInflater)
    }

    private val vpAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PicketDayAdapter() {
           showPicketDetailDialog(it)
        }
    }

    private val viewModel: PicketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, R.string.toolbar_picket, true)

        setupVp()
        setupFab()
        observePickets()
        observeUsers()
        observePicketDayUiList()
    }

    private fun setupVp() {
        binding.vpPicket.offscreenPageLimit = 3
        binding.vpPicket.adapter = vpAdapter
    }

    private fun setupFab() {
        binding.fabPicket.setOnClickListener {
            navigateToAddPicket()
        }
    }

    private fun observePickets() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observePickets.collectLatest { result ->
                    result.onSuccess {
                        viewModel.setPicketDays(it)
                    }
                }
            }
        }
    }

    private fun observeUsers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeUsers.collectLatest { result ->
                    result.onSuccess {
                        viewModel.setUsers(it)
                    }
                }
            }
        }
    }

    private fun observePicketDayUiList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.picketDayListUi.collectLatest { result ->
                    submitToVpAdapter(result)
                }
            }
        }
    }

    private fun submitToVpAdapter(items: List<PicketDayUi>) {
        val transform = viewModel.transformToUntilAWeek(items)
        vpAdapter.submitList(transform)
    }

    private fun showPicketDetailDialog(picketUi: PicketUi) {
        PicketDetailDialog(this, picketUi)
            .setOnClickBtnEdit {
                navigateToEditPicket(picketUi)
            }
            .setOnClickBtnDelete {
                deletePicket(picketUi.id)
            }
            .show()
    }

    private fun deletePicket(picketId: String) {
        lifecycleScope.launch {
            viewModel.deletePicket(binding.vpPicket.currentItem, picketId)
        }
    }

    private fun navigateToAddPicket() {
        Intent(this, PicketAddEditActivity::class.java)
            .putExtra(PicketAddEditActivity.EXTRA_MODE, PicketAddEditActivity.MODE_ADD)
            .putExtra(PicketAddEditActivity.EXTRA_DAY, binding.vpPicket.currentItem)
            .also {
                startActivity(it)
            }
    }

    private fun navigateToEditPicket(picketUi: PicketUi) {
        Intent(this, PicketAddEditActivity::class.java)
            .putExtra(PicketAddEditActivity.EXTRA_MODE, PicketAddEditActivity.MODE_EDIT)
            .putExtra(PicketAddEditActivity.EXTRA_PICKET, picketUi)
            .putExtra(PicketAddEditActivity.EXTRA_DAY, binding.vpPicket.currentItem)
            .also {
                startActivity(it)
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}