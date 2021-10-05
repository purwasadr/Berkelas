package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemCashDateDetailBinding
import com.alurwa.berkelas.model.CashDateDetailItem

class CashDateDetailAdapter : ListAdapter<CashDateDetailItem, CashDateDetailAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemCashDateDetailBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemCashDateDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position)
            with(binding) {
                cashDateDetailItem = item
                executePendingBindings()
            }

        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<CashDateDetailItem>() {
            override fun areItemsTheSame(
                oldItem: CashDateDetailItem,
                newItem: CashDateDetailItem
            ): Boolean =
                oldItem.uid == newItem.uid

            override fun areContentsTheSame(
                oldItem: CashDateDetailItem,
                newItem: CashDateDetailItem
            ): Boolean =
                oldItem == newItem

        }
    }
}