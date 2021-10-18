package com.alurwa.berkelas.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.databinding.ActivityLoginBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.showError
import com.alurwa.berkelas.ui.main.MainActivity
import com.alurwa.berkelas.ui.signup.SignUpActivity
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
        setupSignUpTv()
    }

    private fun setupBtnLogin() {
        binding.btnLogin.setOnClickListener {
            loginWithEmail()
        }
    }

    private fun setupSignUpTv() {
        binding.tvSignup.setOnClickListener {
            navigateToSignUp()
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
                            Toast.makeText(applicationContext,
                                it.exception.message, Toast.LENGTH_SHORT)
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

    private fun navigateToSignUp() {
        Intent(this, SignUpActivity::class.java)
            .also {
                startActivity(it)
            }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        var stateReturn = true

        when {
            email.isEmpty() -> {
                stateReturn = false
                binding.tilEmail.showError("Email tidak boleh kosong")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                stateReturn = false
                binding.tilEmail.showError("Format email tidak benar")
            }
            else -> {
                binding.tilEmail.removeError()
            }
        }

        when {
            password.isEmpty() -> {
                stateReturn = false
                binding.tilPassword.showError("Password tidak boleh kosong ")
            }
            password.length < 8 -> {
                binding.tilPassword.showError("Minimal karakter password 8 :)")
                stateReturn = false
            }
            else -> {
                binding.tilPassword.removeError()
            }
        }

        return stateReturn
    }

    private fun pbIsVisible(value: Boolean) {
        binding.pb.isVisible = value
        binding.btnLogin.visibility = if(value) View.INVISIBLE else View.VISIBLE
    }

}