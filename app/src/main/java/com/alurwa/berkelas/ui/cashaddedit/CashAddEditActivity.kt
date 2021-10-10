package com.alurwa.berkelas.ui.cashaddedit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityCashAddEditBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.setOnClickForDialog
import com.alurwa.berkelas.extension.showError
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.Result
import com.alurwa.common.model.onError
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onSuccess
import com.alurwa.data.model.CashAddParams
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CashAddEditActivity : AppCompatActivity() {
    private val binding: ActivityCashAddEditBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCashAddEditBinding.inflate(layoutInflater)
    }

    private val viewModel: CashAddEditViewModel by viewModels()

    private var isDoneVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        setupDataBinding()
        setupInputView()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupInputView() {

        // Menghilangkan focus dari semua view agar focus tidak
        // diterapkan pada actDate yang mana akan langsung memanggil
        // metode onClick dan menyebabkan dialog langsung terbuka
        currentFocus?.clearFocus()
        binding.actDate.setOnClickForDialog(this) {
            doChangeDate()
        }

        if (viewModel.mode == MODE_EDIT) {
            binding.actDate.isEnabled = false
        }
    }

    private fun doChangeDate() {
        val selection = viewModel.date.value ?: Date().time

        val picker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Pilih Tanggal")
            .setSelection(selection)
            .build()

        picker.show(supportFragmentManager, "date_of_birth_picker")
        picker.addOnPositiveButtonClickListener {
            viewModel.setDate(it)
        }
    }

    private fun setupToolbar() {
        val title = if (viewModel.mode == MODE_ADD) {
            "Add Cash"
        } else {
            "Edit Cash"
        }

        binding.appbar.toolbar.setupToolbar(this, title, true)
    }

    private fun saveCash() {
        if (!checkValidity()) return

        if (viewModel.mode == MODE_ADD) {
            addCash()
        } else if (viewModel.mode == MODE_EDIT) {
            editCash()
            finish()
        } else {
            throw IllegalStateException()
        }
    }

    // Mengecek input - input text apakah text didalamnya valid untuk
    // dilanjutkan ke proses input data
    private fun checkValidity(): Boolean {
        var isValid = true

        if (binding.edtAmount.text.toString().isEmpty()) {
            isValid = false
            binding.tilAmount.showError(R.string.error_cause_empty)
        } else {
            binding.tilAmount.removeError()
        }

        return isValid
    }

    private fun addCash() {
        lifecycleScope.launch {
            viewModel.addCash(inputToCashAddParams()).collectLatest {
                resultResponse(it)
            }
        }
    }

    private fun editCash() {
        lifecycleScope.launch {
            viewModel.editCash(inputToCashEditParams())
        }
    }

    private fun resultResponse(result: Result<Boolean>) {
        result.onSuccess {
            finish()
        }.onLoading {
            setMenuDoneVisibility(false)
        }.onError {
            setMenuDoneVisibility(true)
        }
    }

    // Memasukkan input - input view ke model
    private fun inputToCashAddParams() =
        CashAddParams(
            date = viewModel.date.value,
            amount = binding.edtAmount.text.toString().toInt(),
            hasPaid = emptyList()
        )

    private fun inputToCashEditParams() =
        viewModel.cash!!.copy(
            amount = binding.edtAmount.text.toString().toInt(),
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
        return when (item.itemId) {
            R.id.menu_done -> {
                saveCash()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_MODE = "extra_mode"
        const val EXTRA_CASH = "extra_cash"
        const val MODE_ADD = 0
        const val MODE_EDIT = 1
    }
}