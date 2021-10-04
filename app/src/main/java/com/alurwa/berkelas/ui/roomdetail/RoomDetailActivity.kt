package com.alurwa.berkelas.ui.roomdetail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityRoomDetailBinding
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onError
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onSuccess
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomDetailActivity : AppCompatActivity() {

    private val binding: ActivityRoomDetailBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRoomDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: RoomDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "", true)

        setupViews()
    }

    private fun setupViews() {
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.fab.setOnClickListener {

            if (!viewModel.isChoice) {
                doInputPassword()
            } else {
                SnackbarUtil.showShort(binding.root, "Room ini sudah dipilih")
            }
        }
    }

    private fun doInputPassword() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_room_password, null)

        MaterialAlertDialogBuilder(this)
            .setTitle("Masukkan Password")
            .setView(dialogView)
            .setPositiveButton("Oke") { dialog, _ ->
                dialog.dismiss()

                val edtPass =
                    dialogView.findViewById<TextInputEditText>(R.id.edt_password).text.toString()

                if (verifyPassword(edtPass)) {
                    setRoomToUser()
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun verifyPassword(password: String): Boolean {
        val passwordSet = viewModel.room.password
        return if (passwordSet == password) {
            true
        } else {
            SnackbarUtil.showShort(binding.root, "Password yang anda masukkan salah")
            false
        }
    }

    private fun setRoomToUser() {

        lifecycleScope.launch {
            viewModel.applyRoom2().collectLatest {
                it.onSuccess {
                    SnackbarUtil.showShort(binding.root, "Berhasil masuk room")
                }.onLoading {
                    SnackbarUtil.showShort(binding.root, "Loading")
                }.onError {
                    SnackbarUtil.showShort(binding.root, "Error")
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ROOM = "extra_room"
        const val EXTRA_IS_CHOICE = "extra_is_choice"
    }
}