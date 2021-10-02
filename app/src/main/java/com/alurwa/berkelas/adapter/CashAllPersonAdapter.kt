package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemCashAllBinding
import com.alurwa.berkelas.model.CashAllPersonItem

class CashAllPersonAdapter(
    private val onClick: (subjectItem: CashAllPersonItem) -> Unit
) : ListAdapter<CashAllPersonItem, CashAllPersonAdapter.ViewHolder>(COMPARATOR) {

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
            val item = getItem(position)
            with(binding) {
                txtName.text = item.name
                txtCount.text = item.amount.toString()
                root.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<CashAllPersonItem>() {
            override fun areItemsTheSame(
                oldItem: CashAllPersonItem,
                newItem: CashAllPersonItem
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CashAllPersonItem,
                newItem: CashAllPersonItem
            ): Boolean =
                oldItem == newItem

        }
    }
}