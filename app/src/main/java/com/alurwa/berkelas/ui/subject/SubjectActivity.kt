package com.alurwa.berkelas.ui.subject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivitySubjectBinding

class SubjectActivity : AppCompatActivity() {
    private val binding: ActivitySubjectBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySubjectBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}