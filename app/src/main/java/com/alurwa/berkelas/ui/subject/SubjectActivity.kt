package com.alurwa.berkelas.ui.subject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.adapter.SubjectVpAdapter
import com.alurwa.berkelas.databinding.ActivitySubjectBinding
import com.alurwa.berkelas.model.SubjectVpItem
import com.alurwa.berkelas.util.setupToolbar

class SubjectActivity : AppCompatActivity() {
    private val binding: ActivitySubjectBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySubjectBinding.inflate(layoutInflater)
    }

    private val vpAdapter: SubjectVpAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SubjectVpAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setupToolbar(this, "Pelajaran", true)

        setupVp()
    }

    private fun setupVp() {
        binding.vpListSubject.offscreenPageLimit = 3
        binding.vpListSubject.adapter = vpAdapter
    }


    private fun submitVpAdapter(vpList: List<SubjectVpItem>) {
        vpAdapter.submitList(vpList)
    }
}