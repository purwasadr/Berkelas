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
import com.alurwa.berkelas.adapter.CashAllPersonAdapter
import com.alurwa.berkelas.databinding.FragmentCashPersonBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CashPersonFragment : Fragment() {
    private var _binding: FragmentCashPersonBinding? = null

    private val binding get() = _binding!!


    private val adapter: CashAllPersonAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CashAllPersonAdapter {

        }
    }

    private val viewModel: CashAllViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCashPersonBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fillAdapter()
    }

    private fun setupRecyclerView() {
        with(binding.listCashPerson) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@CashPersonFragment.adapter
        }
    }

    private fun fillAdapter() {
       viewLifecycleOwner.lifecycleScope.launch {
           repeatOnLifecycle(Lifecycle.State.STARTED) {
               viewModel.userCashCombine.collectLatest {
                   adapter.submitList(it)
               }
           }
        }

    }

    companion object {

    }
}