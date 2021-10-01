package com.alurwa.berkelas.ui.cashall

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.adapter.CashAllAdapter
import com.alurwa.berkelas.databinding.ActivityCashAllBinding
import com.alurwa.berkelas.ui.cashaddedit.CashAddEditActivity
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CashAllActivity : AppCompatActivity() {

    private val binding: ActivityCashAllBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCashAllBinding.inflate(layoutInflater)
    }

    private val viewModel: CashAllViewModel by viewModels()

    private val adapter: CashAllAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CashAllAdapter {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(
            this,
            "Daftar kas",
            true
        )

        setupRecyclerView()
        setupFab()

        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeUserByRoomId.collectLatest { result ->
                    result.onSuccess {
                        viewModel.setUsers(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cash.collectLatest { result ->
                    result.onSuccess {
                        viewModel.setCashList(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userCashCombine.collectLatest {
                    adapter.submitList(it)
                    Timber.d("Item1 Amount : " + it.get(0).amount.toString())
                }
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding.listCashAll) {
            layoutManager = LinearLayoutManager(this@CashAllActivity)
            setHasFixedSize(true)
            adapter = this@CashAllActivity.adapter
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            navigateToAddCash()
        }
    }

    private fun navigateToAddCash() {
        Intent(this, CashAddEditActivity::class.java)
            .putExtra(CashAddEditActivity.EXTRA_MODE, CashAddEditActivity.MODE_ADD)
            .also {
                startActivity(it)
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}