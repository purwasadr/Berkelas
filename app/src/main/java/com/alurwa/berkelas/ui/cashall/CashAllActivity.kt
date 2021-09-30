package com.alurwa.berkelas.ui.cashall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityCashAllBinding

class CashAllActivity : AppCompatActivity() {

    private val binding: ActivityCashAllBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCashAllBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}