package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ListItemCashAllBinding
import com.alurwa.berkelas.databinding.ListItemCashAllDateBinding
import com.alurwa.berkelas.model.CashAllItem

class CashAllAdapter(
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<CashAllItem, RecyclerView.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.list_item_cash_all_date -> {
                ListItemCashAllDateBinding.inflate(
                    layoutInflater, parent, false
                ).let {
                    CashViewHolder(it)
                }
            }

            else -> {
                ListItemCashAllBinding.inflate(
                    layoutInflater, parent, false
                ).let {
                    PeopleViewHolder(it)
                }
            }
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is CashViewHolder -> holder.bind(item as CashAllItem.Cash)
            is PeopleViewHolder -> holder.bind(item as CashAllItem.People)
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CashAllItem.Cash -> R.layout.list_item_cash_all_date
        is CashAllItem.People -> R.layout.list_item_cash_all
        else -> throw IllegalStateException("Unknown view")
    }

    inner class PeopleViewHolder(
        private val binding: ListItemCashAllBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CashAllItem.People) {
            with(binding) {
                tvName.text = item.name
                tvCount.text = item.amount.toString()
            }

        }
    }

    inner class CashViewHolder(
        private val binding: ListItemCashAllDateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CashAllItem.Cash) {
            with(binding) {

            }

        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<CashAllItem>() {
            override fun areItemsTheSame(oldItem: CashAllItem, newItem: CashAllItem): Boolean {
                val item1 =
                    oldItem is CashAllItem.Cash && newItem is CashAllItem.Cash && oldItem.id == newItem.id

                val item2 =
                    oldItem is CashAllItem.People && newItem is CashAllItem.People && oldItem.id == newItem.id

                return item1 || item2
            }

            override fun areContentsTheSame(oldItem: CashAllItem, newItem: CashAllItem): Boolean =
                oldItem == newItem

        }

        const val TYPE_AS_CASH = 100
        const val TYPE_AS_PEOPLE = 101
    }
}