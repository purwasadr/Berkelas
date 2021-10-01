package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemCashAllBinding
import com.alurwa.berkelas.model.CashAllItem

class CashAllAdapter (
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<CashAllItem, CashAllAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemCashAllBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemCashAllBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            with(binding) {
                val data = getItem(position)
                txtName.text = data.name
                txtCount.text = data.amount.toString()
                root.setOnClickListener {
                    onItemClick(position)
                }
            }

        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<CashAllItem>() {
            override fun areItemsTheSame(oldItem: CashAllItem, newItem: CashAllItem): Boolean =
                oldItem.id  == newItem.id

            override fun areContentsTheSame(oldItem: CashAllItem, newItem: CashAllItem): Boolean =
                oldItem == newItem

        }
    }
}