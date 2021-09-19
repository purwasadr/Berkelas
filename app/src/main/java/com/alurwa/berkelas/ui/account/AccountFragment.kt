package com.alurwa.berkelas.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.adapter.AccountMenuAdapter
import com.alurwa.berkelas.databinding.FragmentAccountBinding
import com.alurwa.berkelas.model.MainMenuItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    private val accountMenuAdapter: AccountMenuAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AccountMenuAdapter {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAccountMenu()

        fillAccountMenuAdapter()
    }

    private fun setupAccountMenu() {
        with(binding) {
            listMenuAccount.setHasFixedSize(true)
            listMenuAccount.layoutManager = LinearLayoutManager(requireContext())
            listMenuAccount.adapter = accountMenuAdapter
        }
    }

    private fun fillAccountMenuAdapter() {
        val accountMenuList = listOf(
            MainMenuItem(
                "Akun Saya",
                true
            ),
            MainMenuItem(
                "Setting",
                true
            )
        )

        accountMenuAdapter.submitList(accountMenuList)
    }
}