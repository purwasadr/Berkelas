package com.alurwa.berkelas.ui.roomdetail

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityRoomDetailBinding
import com.alurwa.berkelas.ui.roomaddedit.RoomAddEditActivity
import com.alurwa.berkelas.util.Role
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.*
import com.alurwa.data.model.RoomSetParams
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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
            val roomId = viewModel.room.value?.id

            if (userRoomId != roomId) {
                doInputPassword()
            } else {
                removePasswordDialog()
            }
        }
    }

    private fun observe() {
        viewModel.setIsLoading(true)

        lifecycleScope.launch {
            viewModel.userOther.collectLatest { result ->
                result.onSuccess {
                    val user = it

                    if (user != null) {
                        viewModel.setCreatorName(user.fullName)
                    }
                }
            }

            viewModel.observeRoom.combine(viewModel.observeUser) { roomP, userP ->
                Pair(roomP, userP)
            }.collectLatest { result ->
                result.first.onSuccess {
                    if (it != null) {
                        viewModel.setRoom(it)
                    }
                }

                result.second.onSuccess {
                    val user = it
                    if (user != null) {
                        viewModel.setUser(user)
                        changePropertyFab(user.roomId)
                    }
                }

                if (result.first !is Result.Loading && result.second !is Result.Loading) {
                    viewModel.setIsLoading(false)
                }
            }
        }
    }

    private fun changePropertyFab(userRoomId: String) {
        val roomId = viewModel.room.value?.id
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
            .setMessage("Anda yakin keluar dari room ini?")
            .setPositiveButton("Keluar") { dialog, _ ->
                removeRoomToUser()
                dialog.dismiss()
            }.setNegativeButton(R.string.btn_cancel) { dialog, _ ->
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
            .setPositiveButton("Masuk") { dialog, _ ->
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
        val passwordSet = viewModel.room.value?.password
        return if (passwordSet == password) {
            true
        } else {
            SnackbarUtil.showShort(binding.root, R.string.error_wrong_password)
            false
        }
    }

    private fun deleteRoomDialog(action: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.title_delete_room)
            .setPositiveButton(R.string.btn_delete) { dialog, _ ->
                action()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteRoom() {
        deleteRoomDialog {
            viewModel.deleteRoom()
            finish()
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
        val roomExtra = viewModel.roomExtra

        val isRoomOwner = roomExtra.creatorId == viewModel.user.value?.uid

        val roomSetParams = RoomSetParams(
            roomId = roomExtra.id,
            role = Role.MEMBER.code,
            isRoomOwner = isRoomOwner
        )

        lifecycleScope.launch {
            viewModel.applyRoom(roomSetParams).collectLatest {
                it.onSuccess {
                    SnackbarUtil.showShort(binding.root, "Berhasil masuk room")
                }.onLoading {

                }.onError {
                    SnackbarUtil.showShort(binding.root, "Error")
                }
            }
        }
    }

    private fun navigateToRoomEdit(roomData: RoomData) {
        Intent(this, RoomAddEditActivity::class.java)
            .putExtra(RoomAddEditActivity.EXTRA_MODE, RoomAddEditActivity.MODE_EDIT)
            .putExtra(RoomAddEditActivity.EXTRA_ROOM, roomData)
            .also {
                startActivity(it)
            }

    }

//    private fun uiLoading(value: Boolean) {
//        binding.maskWhite.isVisible = value
//        binding.fab.isVisible = !value
//    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_room_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_delete -> {
                deleteRoom()
                true
            }
            R.id.menu_edit -> {
                navigateToRoomEdit(viewModel.room.value!!)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_ROOM = "extra_room"
        const val EXTRA_IS_CHOICE = "extra_is_choice"
    }
}