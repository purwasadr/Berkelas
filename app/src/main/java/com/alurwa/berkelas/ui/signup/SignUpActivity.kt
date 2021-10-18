package com.alurwa.berkelas.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivitySignUpBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.setOnClickForDialog
import com.alurwa.berkelas.extension.showError
import com.alurwa.berkelas.ui.main.MainActivity
import com.alurwa.berkelas.util.Gender
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.common.model.SignUpParams
import com.alurwa.common.model.onError
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onSuccess
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
    private val confirmPassword get() = binding.edtConfirmPassword.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViews()
        setupInputView()
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnSignUp.setOnClickListener {
            doSignUp()
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun doSignUp() {
        if (!checkValidity()) return

        lifecycleScope.launch {
            viewModel.signUpWithEmail(
                inputToSignUpParam()
            ).collectLatest { result ->
                result.onSuccess {
                    navigateToMain()
                    finishAffinity()
                }.onLoading {
                    pbIsVisible(true)
                }.onError {
                    pbIsVisible(false)
                    SnackbarUtil.showShort(binding.root, it.message)
                }
            }
        }
    }

    private fun inputToSignUpParam() =
        SignUpParams(
            email = email,
            password = password,
            username = username,
            fullName = fullName,
            nickname = nickName,
            dateOfBirth = viewModel.dateOfBirth.value,
            gender = viewModel.gender.value
        )

    private fun checkValidity(): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            isValid = false
            binding.tilEmail.showError(R.string.error_cause_empty)

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false
            binding.tilEmail.showError(R.string.error_invalid_email_format)
        } else {
            binding.tilEmail.removeError()
        }

        if (fullName.isEmpty()) {
            isValid = false
            binding.tilFullname.showError(R.string.error_cause_empty)
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
            binding.tilConfirmPassword.showError(R.string.error_cause_empty)
        } else {
            binding.tilUsername.removeError()
        }

        when {
            password.isEmpty() -> {
                isValid = false
                binding.tilPassword.showError("Password tidak boleh kosong ")
            }
            password.length < 8 -> {
                binding.tilPassword.showError("Minimal karakter password 8")
                isValid = false
            }
            else -> {
                binding.tilPassword.removeError()
            }
        }

        when {
            confirmPassword.isEmpty() -> {
                isValid = false
                binding.tilConfirmPassword.showError(R.string.error_cause_empty)
            }
            password != confirmPassword -> {
                isValid = false
                binding.tilConfirmPassword.showError(R.string.error_confirm_password_not_same)
            }
            else -> {
                binding.tilConfirmPassword.removeError()
            }
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
            .setTitle(R.string.title_chose_gender)
            .setSingleChoiceItems(arrayItems, selectedItem) { dialog, which ->
                viewModel.setGender(Gender.values()[which].code)
                dialog.dismiss()
            }
            .show()
    }

    private fun doChangeDateOfBirth() {
        val now = viewModel.dateOfBirth.value ?: Date().time

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.title_chose_date)
            .setSelection(now)
            .build()

        picker.show(supportFragmentManager, "date_of_birth_picker")
        picker.addOnPositiveButtonClickListener {
            viewModel.setDateOfBirth(it)
        }
    }

    private fun navigateToMain() {
        Intent(this, MainActivity::class.java)
            .also {
                startActivity(it)
            }
    }

    private fun pbIsVisible(value: Boolean) {
        binding.pb.isVisible = value
        binding.btnSignUp.visibility = if (value) View.INVISIBLE else View.VISIBLE
    }
}