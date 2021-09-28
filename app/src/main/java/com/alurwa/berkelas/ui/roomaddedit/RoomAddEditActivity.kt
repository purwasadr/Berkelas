package com.alurwa.berkelas.ui.roomaddedit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityRoomAddEditBinding
import com.alurwa.berkelas.util.setupToolbar

class RoomAddEditActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRoomAddEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.toolbar.setupToolbar(this, "Tambah Room", true)


    }
}