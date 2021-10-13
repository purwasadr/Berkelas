package com.alurwa.berkelas.ui.picketaddedit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityPicketAddEditBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.setOnClickForDialog
import com.alurwa.berkelas.extension.showError
import com.alurwa.berkelas.extension.singleChoiceItems
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.*
import com.alurwa.data.model.PicketAddParams
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PicketAddEditActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPicketAddEditBinding.inflate(layoutInflater)
    }

    private val viewModel: PicketAddEditViewModel by viewModels()

    private var isDoneVisible = true

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
        currentFocus?.clearFocus()

        binding.actStudent.setOnClickForDialog(this) {
            getUser {
                changeAccount(it)
            }
        }

        binding.actDay.setOnClickForDialog(this) {
            doChangeDay()
        }
    }

    private fun getUser(action: (users: List<User>) -> Unit) {
        lifecycleScope.launch {
            viewModel.getUsersByRoomId().collectLatest { result ->
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

        val selectedItem = userList.indexOfFirst {
            it.uid == viewModel.selectedUser.value?.uid
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih Akun")
            .singleChoiceItems(arrayUsers, selectedItem) { which ->
                viewModel.setSelectedUser(userList[which])
            }
            .show()
    }

    private fun doChangeDay() {
        val dayOfWeek = resources.getStringArray(R.array.day_of_week)
        val day = viewModel.day.value

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih hari")
            .setSingleChoiceItems(dayOfWeek, day) { dialog, which ->
                viewModel.setDay(which)
                dialog.dismiss()
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

    private fun addPicket() {
        lifecycleScope.launch {
            viewModel.addPicket(inputToPicketAdd()).collectLatest { result ->
                resultResponse(result)
            }
        }
    }

    private fun editPicket() {
        lifecycleScope.launch {
            viewModel.editPicket(inputToPicketEdit()).collectLatest { result ->
                resultResponse(result)
            }
        }
    }

    private fun resultResponse(result: Result<Boolean>) {
        result.onSuccess {
            finish()
        }.onLoading {
            setMenuDoneVisibility(false)
        }.onError {
            setMenuDoneVisibility(true)
        }
    }

    private fun checkValidity(): Boolean {
        var isValid = true

        if (viewModel.selectedUser.value == null) {
            binding.tilStudent.showError("Pilih dahulu muridnya")
            isValid = false
        } else {
            binding.tilStudent.removeError()
        }

        return isValid
    }

    private fun inputToPicketAdd() = PicketAddParams(
        userId = viewModel.selectedUser.value!!.uid, note = binding.edtNote.text.toString()
    )

    private fun inputToPicketEdit() = viewModel.picket!!.run {
        Picket(
            id = id, userId = viewModel.selectedUser.value!!.uid, note = note
        )
    }

    private fun setMenuDoneVisibility(value: Boolean) {
        isDoneVisible = value
        invalidateOptionsMenu()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done_common, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_done)?.isVisible = isDoneVisible
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {
                if (!checkValidity()) return true

                if (viewModel.mode == MODE_ADD) {
                    addPicket()
                } else {
                    editPicket()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }


    }

    companion object {
        const val EXTRA_MODE = "EXTRA_MODE"
        const val EXTRA_PICKET = "EXTRA_PICKET"
        const val EXTRA_DAY = "EXTRA_DAY"
        const val MODE_ADD = 0
        const val MODE_EDIT = 1

    }

}