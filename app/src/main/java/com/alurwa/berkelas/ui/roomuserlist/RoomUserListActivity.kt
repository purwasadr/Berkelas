package com.alurwa.berkelas.ui.roomuserlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.adapter.RoomUserAdapter
import com.alurwa.berkelas.databinding.ActivityRoomUserListBinding
import com.alurwa.berkelas.ui.roomuserdetail.RoomUserDetailActivity
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomUserListActivity : AppCompatActivity() {
    // Use lazy with no thread-safe
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRoomUserListBinding.inflate(layoutInflater)
    }

    private val viewModel: RoomUserListViewModel by viewModels()

    private val adapter: RoomUserAdapter by lazy(LazyThreadSafetyMode.NONE) {
        RoomUserAdapter() {
            navigateToRoomUserDetail(it.uid)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "Daftar user", true)

        setupList()
        observeUsersInThisRoom()
    }

    // Setup RecyclerView
    private fun setupList() {
        binding.listRoomUser.setHasFixedSize(true)
        binding.listRoomUser.layoutManager = LinearLayoutManager(this)
        binding.listRoomUser.adapter = adapter
    }

    // observe Users yang berada di room yang sama
    private fun observeUsersInThisRoom() {
        lifecycleScope.launch {
            viewModel.users.collectLatest { result ->
                result.onSuccess {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun navigateToRoomUserDetail(id: String) {
        Intent(this, RoomUserDetailActivity::class.java)
            .putExtra(RoomUserDetailActivity.EXTRA_USER_ID, id)
            .also {
                startActivity(it)
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}