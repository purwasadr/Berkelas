package com.alurwa.berkelas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.alurwa.berkelas.adapter.MainCardAdapter
import com.alurwa.berkelas.adapter.MainMenuAdapter
import com.alurwa.berkelas.databinding.FragmentHomeBinding
import com.alurwa.berkelas.model.MainMenuItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val mainMenuAdapter: MainMenuAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainMenuAdapter {

        }
    }

    private val mainHeaderCardAdapter: MainCardAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainCardAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListMenu()
        setupHeaderCard()
        fillMenuMainAdapter()
        fillMainHeaderCard()
    }

    private fun setupListMenu() {
        with(binding) {
            listMenu.setHasFixedSize(true)
            listMenu.layoutManager = GridLayoutManager(requireContext(), 3)
            listMenu.adapter = mainMenuAdapter
        }
    }

    private fun setupHeaderCard() {
        with(binding) {
            listCard.setHasFixedSize(true)
            listCard.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            listCard.adapter = mainHeaderCardAdapter

            PagerSnapHelper().attachToRecyclerView(listCard)
        }
    }

    private fun fillMenuMainAdapter() {
        val menuMains = listOf(
            MainMenuItem(
                name = "Jadwal",
                isActive = true
            ),

            MainMenuItem(
                name = "Tugas",
                isActive = true
            )
        )

        mainMenuAdapter.submitList(
            menuMains
        )
    }

    private fun fillMainHeaderCard() {
        val headerList = listOf(
            "Haaaa",
            "Opep",
            "Why"
        )

        mainHeaderCardAdapter.submitList(headerList)
    }
}