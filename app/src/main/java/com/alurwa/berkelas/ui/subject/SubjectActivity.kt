package com.alurwa.berkelas.ui.subject

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alurwa.berkelas.adapter.SubjectVpAdapter
import com.alurwa.berkelas.databinding.ActivitySubjectBinding
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.Subject
import com.alurwa.common.model.onError
import com.alurwa.common.model.onLoading
import com.alurwa.common.model.onSuccess
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SubjectActivity : AppCompatActivity() {
    private val binding: ActivitySubjectBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySubjectBinding.inflate(layoutInflater)
    }

    private val vpAdapter: SubjectVpAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SubjectVpAdapter()
    }

    private val viewModel: SubjectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setupToolbar(this, "Pelajaran", true)

        setupVp()

        observeSubject()
    }


    private fun setupVp() {
        binding.vpListSubject.offscreenPageLimit = 3
        binding.vpListSubject.adapter = vpAdapter
    }

    private fun observeSubject() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeSubject.collectLatest {
                    it.onSuccess { data ->
                        submitVpAdapter(data)
                    }.onLoading {

                    }.onError {

                    }
                }
            }
        }
    }


    private fun submitVpAdapter(vpList: List<Subject>) {
        vpAdapter.submitList(vpList)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}