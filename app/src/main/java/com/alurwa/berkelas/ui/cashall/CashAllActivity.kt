package com.alurwa.berkelas.ui.cashall

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alurwa.berkelas.R
import com.alurwa.berkelas.adapter.CashAllFragmentAdapter
import com.alurwa.berkelas.databinding.ActivityCashAllBinding
import com.alurwa.berkelas.ui.cashaddedit.CashAddEditActivity
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onSuccess
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CashAllActivity : AppCompatActivity() {

    private val binding: ActivityCashAllBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCashAllBinding.inflate(layoutInflater)
    }

    private val viewModel: CashAllViewModel by viewModels()

    private val vpAdapter: CashAllFragmentAdapter by lazy(LazyThreadSafetyMode.NONE){
        CashAllFragmentAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setupToolbar(
            this,
            R.string.toolbar_kas,
            true
        )

        setupFab()
        setupViewPager()
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
    }

    private fun setupViewPager() {
        binding.vpCashAll.adapter = vpAdapter

        // Menggunakan TabLayoutMediator untuk mengikat TabLayout dan ViewPager
        // dan secara otomatis membuat tab di TabLayout
        TabLayoutMediator(binding.tabLayout, binding.vpCashAll) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_item_member)
                1 -> tab.text = getString(R.string.tab_item_date)
                else -> throw IllegalStateException("Unknown tab")
            }
        }.attach()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cash_all, menu)
        return true
    }
}