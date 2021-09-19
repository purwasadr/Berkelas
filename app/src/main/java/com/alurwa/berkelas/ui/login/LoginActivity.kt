package com.alurwa.berkelas.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.databinding.ActivityLoginBinding
import com.alurwa.berkelas.ui.main.MainActivity
import com.alurwa.common.model.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBtnLogin()
    }

    private fun setupBtnLogin() {
        binding.btnLogin.setOnClickListener {
            loginWithEmail()
        }
    }

    private fun loginWithEmail() {
        val email = binding.edtEmail.text.toString()

        val password = binding.edtPassword.text.toString()

        if (!isInputValid(email, password)) return

        lifecycleScope.launch {
            viewModel.loginWithEmail(email, password)
                .collectLatest {
                    when (it) {
                        is Result.Success -> {
                            navigateToMain()
                            finish()
                        }

                        is Result.Loading -> {
                            pbIsVisible(true)
                        }

                        is Result.Error -> {
                            Toast.makeText(applicationContext, it.exception.message, Toast.LENGTH_SHORT)
                                .show()
                            pbIsVisible(false)
                        }
                    }
                }
        }
    }

    private fun navigateToMain() {
        Intent(this, MainActivity::class.java)
            .also {
                startActivity(it)
            }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        var stateReturn = true

        when {
            email.isEmpty() -> {
                binding.tilPassword.isErrorEnabled = true
                binding.tilEmail.error = "Email tidak boleh kosong"
                stateReturn = false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilPassword.isErrorEnabled = true
                binding.tilEmail.error = "Format email tidak benar"
                stateReturn = false
            }
            else -> {
                binding.tilPassword.isErrorEnabled = false
            }
        }

        when {
            password.isEmpty() -> {
                binding.tilPassword.isErrorEnabled = true
                binding.tilPassword.error = "Password tidak boleh kosong "
                stateReturn = false
            }
            password.length <= 6 -> {
                binding.tilPassword.isErrorEnabled = true
                binding.tilPassword.error = "Minimal karakter password 6 :)"
                stateReturn = false
            }
            else -> {
                binding.tilPassword.isErrorEnabled = false
            }
        }

        return stateReturn
    }

    private fun pbIsVisible(value: Boolean) {
        binding.pb.isVisible = value
    }

}