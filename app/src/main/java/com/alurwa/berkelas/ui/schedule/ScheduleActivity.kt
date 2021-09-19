package com.alurwa.berkelas.ui.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alurwa.berkelas.databinding.ActivityScheduleBinding

class ScheduleActivity : AppCompatActivity() {
    private val binding: ActivityScheduleBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityScheduleBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}