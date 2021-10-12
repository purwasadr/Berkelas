package com.alurwa.berkelas.ui.cashdatedetail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.R
import com.alurwa.berkelas.adapter.CashDateDetailAdapter
import com.alurwa.berkelas.databinding.ActivityCashDateDetailBinding
import com.alurwa.berkelas.model.CashDateDetailItem
import com.alurwa.berkelas.ui.cashaddedit.CashAddEditActivity
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.Cash
import com.alurwa.common.model.onError
import com.alurwa.common.model.onSuccess
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CashDateDetailActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCashDateDetailBinding.inflate(layoutInflater)
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        CashDateDetailAdapter()
    }

    private val viewModel: CashDateDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(
            this
            , R.string.toolbar_cash_detail,
            true
        )

        setupBinding()
        setupList()
        setupFab()

        observeData()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupList() {
        // FIXME: setHasFixedSize() menyebabkan daftar tidak tambil jika RecyclerView-nya
        //        berada langsung didalam ConstraintLayout dan ConstraintLayoutnya di dalam
        //        NestedScroolView
       // binding.listCashDateDetail.setHasFixedSize(true)
        binding.listCashDateDetail.layoutManager = LinearLayoutManager(this)
        binding.listCashDateDetail.adapter = adapter

    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            editPaidDialog()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeUsers.collectLatest { result ->
                    result.onSuccess {
                        viewModel.setUsers(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeCash.collectLatest { result ->
                    result.onSuccess {
                        val cash = it
                        if (cash != null) {
                            viewModel.setCash(cash)
                        }
                    }.onError {

                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paidedList.collectLatest {
                    submitAdapter(it)
                }
            }
        }
    }

    private fun submitAdapter(items: List<CashDateDetailItem>) {
        adapter.submitList(items)
    }

    private fun editPaidDialog() {
        val paidedList = viewModel.paidedList.value

        val arrayItems = Array(paidedList.size) {
            paidedList[it].name
        }
        val booleanArray = BooleanArray(paidedList.size) {
            paidedList[it].isPaid
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih yang sudah bayar")
            .setMultiChoiceItems(arrayItems, booleanArray) { _, which, isChecked ->
                booleanArray[which] = isChecked
            }
            .setPositiveButton("Apply") { dialog, _ ->
                val paidedList2 = paidBooleanArrayToUidList(paidedList, booleanArray)

                editPaid(paidedList2)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.btn_back) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun paidBooleanArrayToUidList(
        items: List<CashDateDetailItem>,
        booleanArray: BooleanArray
    ): List<String> {

        return items.mapIndexedNotNull { index, cashDateDetailItem ->
            if (booleanArray[index]) {
                cashDateDetailItem.uid
            } else {
                null
            }
        }
    }

    private fun editPaid(paidedList: List<String>) {
        val cash = viewModel.cash.value

        if (cash != null) {
            viewModel.editCash(
                cash.copy(
                    hasPaid = paidedList
                )
            )
        }
    }

    private fun deleteCashDialog(action: () -> Unit) {
        MaterialAlertDialogBuilder(this)
       //     .setTitle("Delete Cash")
            .setMessage("Apakah anda yakin?")
            .setPositiveButton(R.string.btn_delete) { dialog, _ ->
                action()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteCash() {
        deleteCashDialog {
            val cashId = viewModel.cash.value?.cashId

            if (cashId != null) {
                viewModel.deleteCash(cashId)
                finish()
            }
        }
    }

    private fun navigateToCashEdit(cash: Cash) {
        Intent(this, CashAddEditActivity::class.java)
            .putExtra(CashAddEditActivity.EXTRA_MODE, CashAddEditActivity.MODE_EDIT)
            .putExtra(CashAddEditActivity.EXTRA_CASH, cash)
            .also {
                startActivity(it)
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cash_date_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit -> {
               // editPaidDialog()
                val cash = viewModel.cash.value

                if (cash != null) {
                    navigateToCashEdit(cash)
                } else {
                    SnackbarUtil.showShort(binding.root, "Data belum dimuat")
                }

                true
            }

            R.id.menu_delete -> {
                deleteCash()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_CASH_ID = "EXTRA_CASH_ID"
    }
}