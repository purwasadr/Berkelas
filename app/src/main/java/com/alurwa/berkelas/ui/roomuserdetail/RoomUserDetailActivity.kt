package com.alurwa.berkelas.ui.roomuserdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityRoomDetailBinding

class RoomUserDetailActivity : AppCompatActivity() {
    private val binding: ActivityRoomDetailBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRoomDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}