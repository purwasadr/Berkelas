package com.alurwa.berkelas.ui.choiceschool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityChoiceSchoolBinding

class ChoiceSchoolActivity : AppCompatActivity() {
    private val binding: ActivityChoiceSchoolBinding by lazy {
        ActivityChoiceSchoolBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}