package com.alurwa.berkelas.ui.subject

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alurwa.berkelas.R
import com.alurwa.berkelas.adapter.SubjectVpAdapter
import com.alurwa.berkelas.databinding.ActivitySubjectBinding
import com.alurwa.berkelas.ui.addeditsubject.AddEditSubjectActivity
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SubjectActivity : AppCompatActivity() {
    private val binding: ActivitySubjectBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySubjectBinding.inflate(layoutInflater)
    }

    private val vpAdapter: SubjectVpAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SubjectVpAdapter() {
            showSubjectInfo(it)
        }
    }

    private val viewModel: SubjectViewModel by viewModels()

    private val currentItem get() = binding.vpListSubject.currentItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setupToolbar(
            this,
            getString(R.string.toolbar_title_subject),
            true
        )

        setupVp()
        setupFab()
        observeSubject()
    }


    private fun setupVp() {
        binding.vpListSubject.offscreenPageLimit = 3
        binding.vpListSubject.adapter = vpAdapter
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            navigateToAddSubject()
        }
    }

    private fun observeSubject() {
        lifecycleScope.launch {

            // FIXME: List Adapter not show list correctly when using repeatOnLifecycle
            viewModel.observeSubject
                .distinctUntilChanged()
                .collectLatest { result ->
                    result.onSuccess { data ->
                        submitVpAdapter(data)
                        Timber.d(data.get(2).toString())
                    }.onLoading {

                    }.onError {
                        SnackbarUtil.showShort(binding.root, it.message)
                    }
                }
        }
    }


    private fun submitVpAdapter(vpList: List<Subject>) {

        val transform = List(7) {
            vpList.find { find ->
                find.day == it
            } ?: Subject(
                day = it,
                subjectItem = emptyList()
            )
        }

        vpAdapter.submitList(vpList)
    }

    private fun showSubjectInfo(subjectItem: SubjectItem) {
        SubjectInfoDialog(this, subjectItem)
            .setOnClickBtnEdit {
                navigateToEditSubject(subjectItem)
            }
            .setOnClickBtnDelete {

            }
            .show()
    }

    private fun navigateToAddSubject() {
        Intent(this, AddEditSubjectActivity::class.java)
            .putExtra(AddEditSubjectActivity.EXTRA_DAY, binding.vpListSubject.currentItem)
            .putExtra(AddEditSubjectActivity.EXTRA_MODE, AddEditSubjectActivity.MODE_ADD)
            .also {
                startActivity(it)
            }
    }

    private fun navigateToEditSubject(subjectItem: SubjectItem) {
        Intent(this, AddEditSubjectActivity::class.java)
            .putExtra(AddEditSubjectActivity.EXTRA_DAY, binding.vpListSubject.currentItem)
            .putExtra(AddEditSubjectActivity.EXTRA_MODE, AddEditSubjectActivity.MODE_EDIT)
            .putExtra(AddEditSubjectActivity.EXTRA_SUBJECT, subjectItem)
            .also {
                startActivity(it)
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}