package com.alurwa.berkelas.ui.subject

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alurwa.berkelas.R
import com.alurwa.berkelas.adapter.SubjectVpAdapter
import com.alurwa.berkelas.databinding.ActivitySubjectBinding
import com.alurwa.berkelas.ui.addeditsubject.AddEditSubjectActivity
import com.alurwa.berkelas.util.Role
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

        binding.appbar.toolbar.setupToolbar(
            this,
            R.string.toolbar_subject,
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
        binding.fab.isVisible = isCanEdit()
        binding.fab.setOnClickListener {
            navigateToAddSubject()
        }
    }

    private fun observeSubject() {
        lifecycleScope.launch {

            // FIXME: List Adapter not show list correctly when using repeatOnLifecycle
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeSubject
                    .distinctUntilChanged()
                    .collectLatest { result ->
                        result.onSuccess { data ->
                            submitVpAdapter(data)
                            Timber.d(data.getOrNull(0).toString())
                        }.onLoading {

                        }.onError {
                            SnackbarUtil.showShort(binding.root, it.message)
                        }
                    }
            }
        }

    }

    private fun isCanEdit(): Boolean {
        val lis = arrayOf(
            Role.LEADER.code,
            Role.CO_LEADER.code,
            Role.SECRETARY.code
        )

        return lis.contains(viewModel.role.role) || viewModel.role.isRoomOwner
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

        vpAdapter.submitList(transform)
    }

    private fun showSubjectInfo(subjectItem: SubjectItem) {
        SubjectInfoDialog(this, subjectItem, isCanEdit())
            .setOnClickBtnEdit {
                navigateToEditSubject(subjectItem)
            }
            .setOnClickBtnDelete {
                deleteSubject(subjectItem.id)
            }
            .show()
    }

    private fun deleteSubject(id: String) {
        lifecycleScope.launch {
            viewModel.deleteSubject(day = currentItem, id).collectLatest {
                it.onSuccess {
                    SnackbarUtil.showShort(binding.root, "Pelajaran telah dihapus")
                }.onError {
                    SnackbarUtil.showShort(binding.root, "Pelajaran gagal dihapus")
                }
            }
        }
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
        onBackPressed()
        return true
    }
}