package com.alurwa.berkelas.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alurwa.berkelas.ui.cashall.CashDateFragment
import com.alurwa.berkelas.ui.cashall.CashPersonFragment

class CashAllFragmentAdapter(
    fm: FragmentActivity
) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> CashPersonFragment()
            1 -> CashDateFragment()
            else -> throw IllegalStateException("Out of index")
        }
}