package com.alurwa.berkelas.ui.picket

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alurwa.berkelas.adapter.PicketDayAdapter
import com.alurwa.berkelas.databinding.ActivityPicketBinding
import com.alurwa.berkelas.util.setupToolbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PicketActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPicketBinding.inflate(layoutInflater)
    }

    private val vpAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PicketDayAdapter() {

        }
    }

    private val viewModel: PicketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "Picket", true)

        setupVp()
        setupFab()
        observePickets()
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

                }
            }
        }
    }

    private fun submitToVpAdapter() {

        vpAdapter.submitList()
    }

    private fun navigateToAddPicket() {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}