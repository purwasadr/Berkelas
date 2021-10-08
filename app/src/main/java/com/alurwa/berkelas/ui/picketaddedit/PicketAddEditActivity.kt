package com.alurwa.berkelas.ui.picketaddedit

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.databinding.ActivityPicketAddEditBinding
import com.alurwa.berkelas.extension.setOnClickForDialog
import com.alurwa.berkelas.extension.singleChoiceItems
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.User
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onNotLoading
import com.alurwa.common.model.onSuccess
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PicketAddEditActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPicketAddEditBinding.inflate(layoutInflater)
    }

    private val viewModel: PicketAddEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupInput()
        setupBinding()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupInput() {
        binding.actStudent.setOnClickForDialog(this) {
            getUser {
                changeAccount(it)
            }
        }
    }

    private fun getUser(action: (users: List<User>) -> Unit) {
        lifecycleScope.launch {
            viewModel.getUsers().collectLatest { result ->
                result.onSuccess {
                    action(it)
                }.onLoading {
                    viewModel.setIsLoading(true)
                }.onNotLoading {
                    viewModel.setIsLoading(false)
                }
            }
        }
    }

    private fun changeAccount(userList: List<User>) {
        val arrayUsers = Array(userList.size) {
            userList[it].fullName
        }

        val selectedItem = -1

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih Akun")
            .singleChoiceItems(arrayUsers, selectedItem) { which ->
                viewModel.setSelectedUser(userList[which])
            }
            .show()
    }

    private fun setupToolbar() {
        val title = if (viewModel.mode == MODE_ADD) {
            "Add Picket"
        } else {
            "Edit Picket"
        }

        binding.appbar.toolbar.setupToolbar(this, title, true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_MODE = "EXTRA_MODE"
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
        const val MODE_ADD = 0
        const val MODE_EDIT = 1

    }

}