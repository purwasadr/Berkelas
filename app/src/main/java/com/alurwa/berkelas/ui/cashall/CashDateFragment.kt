package com.alurwa.berkelas.ui.cashall

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
import kotlinx.coroutines.launch


class CashDateFragment : Fragment() {

    private var _binding: FragmentCashDateBinding? = null

    private val binding get() = _binding!!

    private val viewModel: CashAllViewModel by activityViewModels()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        CashAllDateAdapter {

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

            }
        }

    }

    companion object {
    }
}