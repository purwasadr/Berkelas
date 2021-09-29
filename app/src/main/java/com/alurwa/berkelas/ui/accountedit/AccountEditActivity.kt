package com.alurwa.berkelas.ui.accountedit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityAccountEditBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.setOnClickForDialog
import com.alurwa.berkelas.extension.showError
import com.alurwa.berkelas.util.DateTimeUtil
import com.alurwa.berkelas.util.Gender
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onError
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onSuccess
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AccountEditActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAccountEditBinding.inflate(layoutInflater)
    }

    private val viewModel: AccountEditViewModel by viewModels()

    private var isDoneVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(
            this,
            getString(R.string.toolbar_title_account_edit),
            true
        )

        setupInputView()
    }

    private fun setupInputView() {

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.actGender.setOnClickForDialog(this) {
            doChangeGender()
        }

        binding.actDateOfBirth.setOnClickForDialog(this) {
            doChangeDateOfBirth()
        }
    }

    private fun doChangeGender() {
        val arrayItems = Array(Gender.values().size) {
            Gender.values()[it].getValue(this)
        }

        val genderValue = viewModel.gender.value

        val selectedItem = genderValue - 1

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih Jenis Kelamin")
            .setSingleChoiceItems(arrayItems, selectedItem) { dialog, which ->
                viewModel.setGender(Gender.values()[which].code)
                dialog.dismiss()
            }
            .show()
    }

    private fun doChangeDateOfBirth() {
        val now = viewModel.dateOfBirth.value ?: Date().time

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal")
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setOpenAt(now)
                    .build()
            )
            .build()


        picker.show(supportFragmentManager, "date_of_birth_picker")
        picker.addOnPositiveButtonClickListener {
            viewModel.setDateOfBirth(it)
            SnackbarUtil.showShort(
                binding.root,
                DateTimeUtil.convertDateMillisToString(it)
            )
        }
    }

    private fun saveUserWS() {
        if (checkValidity()) {
            lifecycleScope.launch {
                viewModel.editUserWithoutSchool(inputToUserWithoutSchool())
                    .collectLatest { result ->
                        result
                            .onSuccess {
                                finish()
                            }
                            .onError {
                                setMenuDoneVisibility(true)
                                SnackbarUtil.showShort(binding.root, it.message)
                            }
                            .onLoading {
                                setMenuDoneVisibility(false)
                            }
                    }
            }
        }
    }

    private fun inputToUserWithoutSchool() =
        viewModel.userWS.copy(
            email = binding.actEmail.text.toString(),
            profileImgUrl = viewModel.profileImgUrl.value,
            username = binding.edtUsername.text.toString(),
            fullName = binding.edtFullname.text.toString(),
            nickname = binding.edtNickName.text.toString(),
            dateOfBirth = viewModel.dateOfBirth.value,
            gender = viewModel.gender.value
        )

    private fun checkValidity(): Boolean {
        var isValid = true
        with(binding) {
            if (edtFullname.text.toString().isEmpty()) {
                isValid = false
                tilFullName.showError(getString(R.string.input_error_subject))
            } else {
                tilFullName.removeError()
            }

            if (edtNickName.text.toString().isEmpty()) {
                isValid = false
                tilNickName.showError(getString(R.string.input_error_cause_empty))
            } else {
                tilNickName.removeError()
            }

            if (edtUsername.text.toString().isEmpty()) {
                isValid = false
                tilUsername.showError("Bidang ini tidak boleh kosong")
            } else {
                tilUsername.removeError()
            }

            if (this@AccountEditActivity.viewModel.gender.value !in 1..2) {
                isValid = false
            }
        }

        return isValid
    }

    private fun setMenuDoneVisibility(value: Boolean) {
        isDoneVisible = value
        invalidateOptionsMenu()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_done)?.isVisible = isDoneVisible
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_account_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {
                saveUserWS()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_USER_WITHOUT_SCHOOL = "extra_user_without_school"
    }
}