package com.alurwa.berkelas.ui.roomlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.R
import com.alurwa.berkelas.adapter.RoomListAdapter
import com.alurwa.berkelas.databinding.ActivityRoomListBinding
import com.alurwa.berkelas.model.RoomItem
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
            navigateToRoomDetail(it)
        }
    }

    private val viewModel: RoomListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(
            this,
            getString(R.string.toolbar_title_room_list)
            , true
        )

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
        val isChoice = room.id == viewModel.getMyRoomId()

        Intent(this, RoomDetailActivity::class.java)
            .putExtra(RoomDetailActivity.EXTRA_ROOM, room)
            .putExtra(RoomDetailActivity.EXTRA_IS_CHOICE, isChoice)
            .also {
                startActivity(it)
            }
    }

    private fun navigateToAddRoom() {
        Intent(this, RoomAddEditActivity::class.java)
            .putExtra(RoomAddEditActivity.EXTRA_MODE, RoomAddEditActivity.MODE_ADD)
            .also {
                startActivity(it)
            }
    }

    private fun submitToAdapter(rooms: List<RoomData>) {
        val myRoomId = viewModel.getMyRoomId()
        val we = rooms.map {
            val aw = it.id == myRoomId

            RoomItem(
                aw,
                it
            )
        }

        val roomResult = we.partition {
            it.checked
        }

        val wwe = roomResult.first + roomResult.second
        adapter.submitList(wwe)
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