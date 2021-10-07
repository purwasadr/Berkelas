package com.alurwa.berkelas.ui.roomuserdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityRoomUserDetailBinding
import com.alurwa.berkelas.util.Role
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onError
import com.alurwa.common.model.onSuccess
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomUserDetailActivity : AppCompatActivity() {
    private val binding: ActivityRoomUserDetailBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRoomUserDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: RoomUserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "", true)

        setupBinding()
        observeUser()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeUser() {
        lifecycleScope.launch {
            viewModel.observeUser.collectLatest { result ->
                result.onSuccess {
                    val user = it

                    if (user != null) {
                        viewModel.setUser(user)
                    }
                }.onError {
                    SnackbarUtil.showShort(binding.root, "Gagal mendapatkan data")
                }
            }
        }
    }

    private fun choseRoleDialog(action: (roleCode: String) -> Unit) {
        val roles = Role.values()

        val itemArray = Array(roles.size) {
            roles[it].toString(this)
        }

        val selectionItem = -1

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih Role")
            .setSingleChoiceItems(itemArray, selectionItem) { dialog, which ->
                action(roles[which].code)
                dialog.dismiss()
            }
            .show()
    }

    private fun editRole() {
        choseRoleDialog {
            viewModel.editRole(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_room_user_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_role -> {
                editRole()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
    }
}