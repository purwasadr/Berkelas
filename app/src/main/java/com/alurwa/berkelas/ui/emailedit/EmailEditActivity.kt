package com.alurwa.berkelas.ui.emailedit

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityEmailEditBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.showError
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onError
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onSuccess
import com.alurwa.data.model.EditEmailParams
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmailEditActivity : AppCompatActivity() {
    private val binding: ActivityEmailEditBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityEmailEditBinding.inflate(layoutInflater)
    }

    private val viewModel: EmailEditViewModel by viewModels()

    private val email get() = binding.edtNewEmail.text.toString()
    private val password get() = binding.edtPassword.text.toString()

    private var isDoneVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "Edit Email", true)
    }

    private fun editEmail() {
        if (!checkValidity()) return

        val editEmailParams = inputToEditEmailParams()

        lifecycleScope.launch {
            viewModel.editEmail(editEmailParams).collectLatest {
                it.onSuccess {
                    setResult(RESULT_EMAIL, Intent().putExtra(EXTRA_EMAIL, editEmailParams.newEmail))
                    finish()
                }.onLoading {
                    setMenuDoneVisibility(false)
                }.onError {
                    setMenuDoneVisibility(true)
                }
            }
        }
    }

    private fun setMenuDoneVisibility(value: Boolean) {
        isDoneVisible = value
        invalidateOptionsMenu()
    }

    private fun checkValidity(): Boolean {
        var isValid = true

        when {
            email.isEmpty() -> {
                isValid = false
                binding.tilNewEmail.showError("Email tidak boleh kosong")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                isValid = false
                binding.tilNewEmail.showError("Format email tidak benar")
            }
            else -> {
                binding.tilNewEmail.removeError()
            }
        }

        when {
            password.isEmpty() -> {
                isValid = false
                binding.tilPassword.showError("Password tidak boleh kosong ")
            }
            password.length < 8 -> {
                binding.tilPassword.showError("Minimal karakter password 8 :)")
                isValid = false
            }
            else -> {
                binding.tilPassword.removeError()
            }
        }

        return isValid
    }

    private fun inputToEditEmailParams() = EditEmailParams(
        newEmail = binding.edtNewEmail.text.toString(),
        password = binding.edtPassword.text.toString()
    )

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_done)?.isVisible = isDoneVisible
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done_common, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_done -> {
                editEmail()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val RESULT_EMAIL = 100
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
    }
}