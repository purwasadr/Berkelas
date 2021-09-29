package com.alurwa.berkelas.ui.signup

import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.databinding.ActivitySignUpBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.setOnClickForDialog
import com.alurwa.berkelas.util.DateTimeUtil
import com.alurwa.berkelas.util.Gender
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.common.model.SignUpParams
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
class SignUpActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private val viewModel: SignUpViewModel by viewModels()

    private val fullName get() = binding.edtFullname.text.toString()
    private val nickName get() = binding.edtNickname.text.toString()
    private val email get() = binding.edtEmail.text.toString()
    private val username get() = binding.edtUsername.text.toString()
    private val password get() = binding.edtPassword.text.toString()
    private val repassword get() = binding.edtRepassword.text.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupInputView()
        setupViews()
    }

    private fun setupInputView() {
        binding.actGender.setOnClickForDialog(this) {
            doChangeGender()
        }

        binding.actDateOfBirth.setOnClickForDialog(this) {
            doChangeDateOfBirth()
        }
    }

    private fun setupViews() {
        binding.btnSignUp.setOnClickListener {
            doSignUp()
        }

        binding.txtLogin.setOnClickListener {
            finish()
        }
    }

    private fun doSignUp() {
        lifecycleScope.launch {
            viewModel.signUpWithEmail(
                inputToSignUpParam()
            ).collectLatest { result ->
                result.onSuccess {

                }.onLoading {

                }
            }
        }
    }

    private fun inputToSignUpParam() =
        SignUpParams(
            email = binding.edtEmail.toString(),
            password = binding.edtPassword.toString(),
            username = binding.edtUsername.toString(),
            fullName = binding.edtFullname.toString(),
            nickname = binding.edtNickname.toString(),
            dateOfBirth = viewModel.dateOfBirth.value,
            gender = viewModel.gender.value
        )

    private fun checkValidity(): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            isValid = false

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false
        } else {
            binding.tilEmail.removeError()
        }

        if (fullName.isEmpty()) {
            isValid = false
        } else {
            binding.tilFullname.removeError()
        }

        if (nickName.isEmpty()) {
            isValid = false
        } else {
            binding.tilNickname.removeError()
        }

        if (username.isEmpty()) {
            isValid = false

        } else {
            binding.tilUsername.removeError()
        }

        if (password.isEmpty()) {
            isValid = false
        } else {
            binding.tilPassword.removeError()
        }

        if (repassword.isEmpty()) {
            isValid = false
        } else if (password != repassword) {
            isValid = false
        } else {
            binding.tilRepassword.removeError()
        }

        return isValid
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
                    .setStart(now)
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
}