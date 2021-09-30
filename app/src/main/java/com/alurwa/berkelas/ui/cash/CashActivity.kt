package com.alurwa.berkelas.ui.cash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityCashBinding

class CashActivity : AppCompatActivity() {
    private val binding: ActivityCashBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}