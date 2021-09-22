package com.alurwa.berkelas.ui.choiceschool

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityChoiceSchoolBinding
import com.alurwa.common.model.onError
import com.alurwa.common.model.onSuccess
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ChoiceSchoolActivity : AppCompatActivity() {
    private val binding: ActivityChoiceSchoolBinding by lazy {
        ActivityChoiceSchoolBinding.inflate(layoutInflater)
    }

    private val viewModel: ChoiceSchoolViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        setupInputView()
    }

    private fun observeSchools() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeSchools.collectLatest {  result ->
                    result.onSuccess {
                        viewModel.setSchoolsData(it)
                    }.onError {
                        viewModel.setSchoolsData(emptyList())
                    }
                }
            }
        }
    }

    private fun setupInputView() {
        lifecycleScope.launch {
            viewModel.selectedSchool.filterNotNull().collectLatest {
                binding.actSchool.setText(it.name)
            }
        }

        lifecycleScope.launch {
            viewModel.selectedKelas.filterNotNull().collectLatest {
                binding.actKelas.setText(it.name)
            }
        }

        binding.actSchool.setOnClickListener {
            doChooseSchool()
        }

        binding.actKelas.setOnClickListener {
            doChoseKelas()
        }

    }

    private fun doChoseKelas() {
        val kelasList = viewModel.selectedSchool.value?.kelasList

        if (kelasList == null) {
            return
        }

        val arrayItems = Array(kelasList.size) {
            kelasList[it].name
        }

        val selectedKelasId = kelasList.indexOfFirst {
            it.id == viewModel.selectedKelas.value?.id
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_title_choose_kelas)
            .setSingleChoiceItems(arrayItems, selectedKelasId) { dialog, which ->
                viewModel.setKelas(kelasList[which])
                dialog.dismiss()
            }
            .show()

    }

    private fun doChooseSchool() {
        val schools = viewModel.schoolData.value

        if (schools == null) {
            return
        }

        val arrayItems = Array(schools.size) {
            schools[it].name
        }

        val selectedKelasId = viewModel.selectedKelas.value

        val selectedIndex = schools.indexOfFirst {
            it.id == selectedKelasId?.id
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih sekolah")
            .setSingleChoiceItems(arrayItems, selectedIndex) { dialog, which ->
                viewModel.setSchool(schools[which])
                dialog.dismiss()
            }
            .show()
    }
}