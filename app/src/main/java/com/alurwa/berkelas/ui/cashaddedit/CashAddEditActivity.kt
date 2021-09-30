package com.alurwa.berkelas.ui.cashaddedit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityCashAddEditBinding

class CashAddEditActivity : AppCompatActivity() {
    private val binding: ActivityCashAddEditBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCashAddEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}