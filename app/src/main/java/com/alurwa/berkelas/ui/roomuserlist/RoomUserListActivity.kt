package com.alurwa.berkelas.ui.roomuserlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityRoomUserListBinding

class RoomUserListActivity : AppCompatActivity() {
    // Use lazy with no thread-safe
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRoomUserListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }

    private fun setupList() {

    }
}