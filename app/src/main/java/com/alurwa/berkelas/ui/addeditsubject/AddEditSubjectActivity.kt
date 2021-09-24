package com.alurwa.berkelas.ui.addeditsubject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityAddEditSubjectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditSubjectActivity : AppCompatActivity() {
    private val binding: ActivityAddEditSubjectBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAddEditSubjectBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun aaww() {

    }
}