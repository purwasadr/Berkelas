package com.alurwa.berkelas.ui.cashall

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.adapter.CashAllDateAdapter
import com.alurwa.berkelas.databinding.FragmentCashDateBinding
import com.alurwa.berkelas.ui.cashaddedit.CashAddEditActivity
import com.alurwa.berkelas.ui.cashdatedetail.CashDateDetailActivity
import com.alurwa.common.model.Cash
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CashDateFragment : Fragment() {

    private var _binding: FragmentCashDateBinding? = null

    private val binding get() = _binding!!

    private val viewModel: CashAllViewModel by activityViewModels()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        CashAllDateAdapter {
            navigateToCashDateDetail(it.id)


            val cash = viewModel.cashList.value

            val cashFinded = cash?.find { cashFind ->
                cashFind.cashId == it.id
            }

            if (cashFinded != null) {
             //   itemOptionDialog(cashFinded)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCashDateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fillAdapter()
    }

    private fun setupRecyclerView() {
        with(binding.listCashDate) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@CashDateFragment.adapter
        }
    }

    private fun fillAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cashUserCombine.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun itemOptionDialog(cash: Cash) {
        val arrayItems = arrayOf("Edit", "Delete")

        MaterialAlertDialogBuilder(requireContext())
            .setItems(arrayItems) { dialog, which ->
                if (which == 0) {
                    navigateToEditCash(cash)
                } else if (which == 1) {
                    deleteCash(cash.cashId)
                }
                dialog.dismiss()
            }.show()

    }

    private fun navigateToEditCash(cash: Cash) {
        Intent(requireContext(), CashAddEditActivity::class.java)
            .putExtra(CashAddEditActivity.EXTRA_CASH, cash)
            .putExtra(CashAddEditActivity.EXTRA_MODE, CashAddEditActivity.MODE_EDIT)
            .also {
                startActivity(it)
            }
    }

    private fun navigateToCashDateDetail(cashId: String) {
        Intent(requireContext(), CashDateDetailActivity::class.java)
            .putExtra(CashDateDetailActivity.EXTRA_CASH_ID, cashId)
            .also {
                startActivity(it)
            }
    }

    private fun deleteCash(cashId: String) {
        viewModel.deleteCash(cashId)
    }
}