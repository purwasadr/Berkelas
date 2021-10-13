package com.alurwa.berkelas.ui.accountedit

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityAccountEditBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.setOnClickForDialog
import com.alurwa.berkelas.extension.showError
import com.alurwa.berkelas.ui.profilecrop.ProfileCropActivity
import com.alurwa.berkelas.util.DateTimeUtil
import com.alurwa.berkelas.util.Gender
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onError
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onSuccess
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class AccountEditActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAccountEditBinding.inflate(layoutInflater)
    }

    private val viewModel: AccountEditViewModel by viewModels()

    private var isDoneVisible = true

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            navigateToGallery()
        }
    }

    private val startPickGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val uri = it.data?.data
        if (uri != null) {

            navigateToProfileCrop(uri)
            Timber.d("result pickGallery")
        }
    }

    private val startCropActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val uri = it.data?.data

        if (uri != null) {
            changeProfileImage(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(
            this,
            getString(R.string.toolbar_account_edit),
            true
        )

        setupInputView()
        setupProfileImage()
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

    private fun setupProfileImage() {
        binding.imgProfile.setOnClickListener {
            optionProfileImageDialog()
        }
    }

    private fun optionProfileImageDialog() {
        val arrayItems = arrayOf(
            "Ambil dari gallery", "Ambil dari Camera"
        )

        MaterialAlertDialogBuilder(this)
            .setItems(arrayItems) { dialog, which ->
                if (which == 0) {
                    pickFromGallery()
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun pickFromGallery() {
        val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            navigateToGallery()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permission)) {
                Timber.d("ShouldRequest")
            } else {
                requestPermission.launch(permission)
            }
        }
    }

    private fun navigateToGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("image/*")
            .addCategory(Intent.CATEGORY_OPENABLE)
        val mimeTypes = arrayOf("image/jpeg", "image/png")

        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        Timber.d("pickFromGallery")

        startPickGallery.launch(Intent.createChooser(intent, "Pilih Gambar"))
    }

    private fun navigateToProfileCrop(uri: Uri) {
        Intent(this, ProfileCropActivity::class.java)
            .also {
                it.data = uri
                startCropActivity.launch(it)
            }
    }

    private fun changeProfileImage(uri: Uri) {
        lifecycleScope.launch {
            viewModel.changeProfileImage(uri).collectLatest {
                it.onSuccess {
                    viewModel.setProfileImgUrl(uri.toString())
                }.onError {
                    SnackbarUtil.showShort(binding.root, "Gagal meperbarui foto profil")
                }
            }
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
        val selection = viewModel.dateOfBirth.value ?: Date().time

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal")
            .setSelection(selection)
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
            nickname = binding.edtNickname.text.toString(),
            dateOfBirth = viewModel.dateOfBirth.value,
            gender = viewModel.gender.value
        )

    private fun checkValidity(): Boolean {
        var isValid = true
        with(binding) {
            if (edtFullname.text.toString().isEmpty()) {
                isValid = false
                tilFullName.showError(getString(R.string.error_subject))
            } else {
                tilFullName.removeError()
            }

            if (edtNickname.text.toString().isEmpty()) {
                isValid = false
                tilNickname.showError(getString(R.string.error_cause_empty))
            } else {
                tilNickname.removeError()
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
        onBackPressed()
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