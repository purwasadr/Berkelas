package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemCashAllAsCashBinding
import com.alurwa.berkelas.model.CashAllDateItem

class CashAllDateAdapter(
    private val onClick: (item: CashAllDateItem) -> Unit
) : ListAdapter<CashAllDateItem, CashAllDateAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemCashAllAsCashBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemCashAllAsCashBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position)
            with(binding) {
                binding.cashAllDate = item
                root.setOnClickListener {

                }
            }

        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<CashAllDateItem>() {
            override fun areItemsTheSame(
                oldItem: CashAllDateItem,
                newItem: CashAllDateItem
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CashAllDateItem,
                newItem: CashAllDateItem
            ): Boolean =
                oldItem == newItem

        }
    }
}