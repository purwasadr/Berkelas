package com.alurwa.berkelas.ui.roomaddedit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityRoomAddEditBinding
import com.alurwa.berkelas.extension.removeError
import com.alurwa.berkelas.extension.showError
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.*
import com.alurwa.common.util.autoId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RoomAddEditActivity : AppCompatActivity() {
    private var isDoneVisible: Boolean = true

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRoomAddEditBinding.inflate(layoutInflater)
    }

    private val viewModel: RoomAddEditViewModel by viewModels()

    private val room get() = viewModel.room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "Tambah Room", true)

        setupInputViews()
    }

    private fun setupInputViews() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun inputToAddRoom() = RoomData(
        id = autoId(),
        roomName = binding.edtName.text.toString(),
        kelasName = binding.edtKelas.text.toString(),
        schoolName = binding.edtSchool.text.toString(),
        password = binding.edtPassword.text.toString(),
        creatorId = ""
    )

    private fun inputToEditRoom() = room!!.copy(
        roomName = binding.edtName.text.toString(),
        kelasName = binding.edtKelas.text.toString(),
        schoolName = binding.edtSchool.text.toString(),
    )

    private fun saveRoom() {
        if (checkValidity()) {
            if (viewModel.mode == MODE_ADD) {
                lifecycleScope.launch {
                    viewModel.addRoom(inputToAddRoom()).collectLatest {
                        resultAction(it)
                    }
                }
            } else if (viewModel.mode == MODE_EDIT) {
                lifecycleScope.launch {
                    viewModel.editRoom(inputToEditRoom()).collectLatest {
                        resultAction(it)
                    }
                }
            }
        }
    }

    private fun resultAction(result: Result<Boolean>) {
        result.onLoading {
            isDoneVisible = false
            invalidateOptionsMenu()
        }.onSuccess {
            finish()
        }.onError {
            isDoneVisible = true
            invalidateOptionsMenu()
            SnackbarUtil.showShort(binding.root, it.message)
            Timber.d(it.message)
        }
    }

    private fun checkValidity(): Boolean {
        var isValid = true

        if (binding.edtName.text.toString().isEmpty()) {
            isValid = false
            binding.tilName.showError(getString(R.string.input_error_cause_empty))
        } else {
            binding.tilName.removeError()
        }

        return isValid
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
        menuInflater.inflate(R.menu.menu_add_edit_subject, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {
                saveRoom()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_MODE = "extra_mode"
        const val EXTRA_ROOM = "extra_room"
        const val MODE_ADD = 0
        const val MODE_EDIT = 1
    }
}