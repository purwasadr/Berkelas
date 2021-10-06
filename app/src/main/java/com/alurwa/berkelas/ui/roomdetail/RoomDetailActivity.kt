package com.alurwa.berkelas.ui.roomdetail

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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

        observe()
    }

    private fun setupViews() {
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.fab.setOnClickListener {
            val userRoomId = viewModel.user.value?.roomId
            val roomId = viewModel.room.id

            if (userRoomId != roomId) {
                doInputPassword()
            } else {
                removePasswordDialog()
            }
        }
    }

    private fun observe() {
        uiLoading(true)

        lifecycleScope.launch {
            viewModel.userOther.collectLatest { result ->
                result.onSuccess {
                    val user = it

                    if (user != null) {
                        viewModel.setCreatorName(user.fullName)
                    }
                }
            }

            viewModel.observeUser.collectLatest { result ->
                result.onSuccess {
                    val user = it
                    if (user != null) {
                        viewModel.setUser(user)
                        changePropertyFab(user.roomId)
                    }

                    uiLoading(false)
                }
            }
        }
    }

    private fun changePropertyFab(userRoomId: String) {
        val roomId = viewModel.room.id
        if (userRoomId != roomId) {
            val typeValue = TypedValue()
            theme.resolveAttribute(R.attr.colorPrimary, typeValue, true)

            binding.fab.backgroundTintList = ColorStateList.valueOf(typeValue.data)
            binding.fab.setImageResource(R.drawable.ic_round_done_24)
        } else {
            binding.fab.setImageResource(R.drawable.ic_round_exit_to_app_24)
            binding.fab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.red_500)
            )
        }
    }

    private fun removePasswordDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Keluar room")
            .setMessage("Anda yakin keluar dari room ini?")
            .setPositiveButton("Keluar") { dialog, _ ->
                removeRoomToUser()
                dialog.dismiss()
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun doInputPassword() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_room_password, null)

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.title_insert_password)
            .setView(dialogView)
            .setPositiveButton("Oke") { dialog, _ ->
                dialog.dismiss()

                val edtPass =
                    dialogView.findViewById<TextInputEditText>(R.id.edt_password).text.toString()

                if (verifyPassword(edtPass)) {
                    setRoomToUser()
                }
            }
            .setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun verifyPassword(password: String): Boolean {
        val passwordSet = viewModel.room.password
        return if (passwordSet == password) {
            true
        } else {
            SnackbarUtil.showShort(binding.root, R.string.error_wrong_password)
            false
        }
    }

    private fun removeRoomToUser() {
        lifecycleScope.launch {
            viewModel.removeRoom().collectLatest {
                it.onSuccess {
                    SnackbarUtil.showShort(binding.root, "Berhasil keluar room")
                }.onLoading {

                }.onError {
                    SnackbarUtil.showShort(binding.root, "Error")
                }
            }
        }
    }

    private fun setRoomToUser() {
        lifecycleScope.launch {
            viewModel.applyRoom2().collectLatest {
                it.onSuccess {
                    SnackbarUtil.showShort(binding.root, "Berhasil masuk room")
                }.onLoading {

                }.onError {
                    SnackbarUtil.showShort(binding.root, "Error")
                }
            }
        }

    }

    private fun uiLoading(value: Boolean) {
        binding.maskWhite.isVisible = value
        binding.fab.isVisible = !value
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