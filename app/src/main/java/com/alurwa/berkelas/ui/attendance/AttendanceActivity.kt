package com.alurwa.berkelas.ui.attendance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityAttendanceBinding

class AttendanceActivity : AppCompatActivity() {

    private val binding: ActivityAttendanceBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAttendanceBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}