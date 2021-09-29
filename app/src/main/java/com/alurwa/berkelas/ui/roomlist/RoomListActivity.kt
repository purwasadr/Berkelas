package com.alurwa.berkelas.ui.roomlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.adapter.RoomListAdapter
import com.alurwa.berkelas.databinding.ActivityRoomListBinding
import com.alurwa.berkelas.ui.choiceroom.ChoiceRoomActivity
import com.alurwa.berkelas.ui.roomaddedit.RoomAddEditActivity
import com.alurwa.berkelas.ui.roomdetail.RoomDetailActivity
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.RoomData
import com.alurwa.common.model.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomListActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRoomListBinding.inflate(layoutInflater)
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        RoomListAdapter() {
//            if (viewModel.mode == MODE_NORMAL) {
//                navigateToRoomDetail(it)
//            } else if (viewModel.mode == MODE_CHOICE) {
//                resultToChoice(it)
//            }

            navigateToRoomDetail(it)
        }
    }

    private val viewModel: RoomListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "Daftar Room", true)

        setupRcvView()
        setupFab()
        getRooms()
    }

    private fun setupRcvView() {
        binding.listRoom.setHasFixedSize(true)
        binding.listRoom.layoutManager = LinearLayoutManager(this)
        binding.listRoom.adapter = adapter
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            navigateToAddRoom()
        }
    }
    private fun getRooms() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rooms.collectLatest { result ->
                    result.onSuccess {
                        submitToAdapter(it)
                    }
                }
            }
        }
    }

    private fun navigateToRoomDetail(room: RoomData) {
        Intent(this, RoomDetailActivity::class.java)
            .putExtra(RoomDetailActivity.EXTRA_ROOM, room)
            .also {
                startActivity(it)
            }
    }

    private fun resultToChoice(roomData: RoomData) {
        val putIntent = Intent().putExtra(ChoiceRoomActivity.EXTRA_ROOM, roomData)

        setResult(100, putIntent)
    }

    private fun navigateToAddRoom() {
        Intent(this, RoomAddEditActivity::class.java)
            .putExtra(RoomAddEditActivity.EXTRA_MODE, RoomAddEditActivity.MODE_ADD)
            .also {
                startActivity(it)
            }
    }

    private fun submitToAdapter(rooms: List<RoomData>) {
        adapter.submitList(rooms)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_MODE = "extra_mode"
        const val MODE_NORMAL = 0
        const val MODE_CHOICE = 1
    }
}