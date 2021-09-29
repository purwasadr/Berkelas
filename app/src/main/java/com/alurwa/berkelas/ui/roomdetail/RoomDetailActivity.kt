package com.alurwa.berkelas.ui.roomdetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityRoomDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomDetailActivity : AppCompatActivity() {

    private val binding: ActivityRoomDetailBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRoomDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: RoomDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {

    }
}