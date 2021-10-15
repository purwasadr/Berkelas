package com.alurwa.berkelas.ui.homecardaddedit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityHomeCardAddEditBinding

class HomeCardAddEditActivity : AppCompatActivity() {
    private val binding: ActivityHomeCardAddEditBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityHomeCardAddEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}