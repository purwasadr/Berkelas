package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemMenuAccountBinding
import com.alurwa.berkelas.model.AccountMenuItem

class AccountMenuAdapter(
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<AccountMenuItem, AccountMenuAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemMenuAccountBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemMenuAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position)

            binding.tvName.text = item.name
            binding.imgItemAccount.setImageResource(item.drawableRes)
            binding.root.setOnClickListener {
                onItemClick(position)
            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<AccountMenuItem>() {
            override fun areItemsTheSame(
                oldItem: AccountMenuItem,
                newItem: AccountMenuItem
            ): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: AccountMenuItem,
                newItem: AccountMenuItem
            ): Boolean =
                oldItem == newItem

        }
    }
}