package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ListItemCardMainAddBinding
import com.alurwa.berkelas.databinding.ListItemCardMainBinding
import com.alurwa.berkelas.model.CardHomeItem

class MainCardAdapter(
    private val onContentClick: (CardHomeItem.Content) -> Unit,
    private val onAddClick: () -> Unit
) : ListAdapter<CardHomeItem, RecyclerView.ViewHolder>(COMPARATOR) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is CardHomeItem.Content -> R.layout.list_item_card_main
        is CardHomeItem.Add -> R.layout.list_item_card_main_add
        else -> throw IllegalStateException("Unknown view")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.list_item_card_main -> {
                ListItemCardMainBinding.inflate(
                    layoutInflater, parent, false
                ).let {
                    ContentViewHolder(it)
                }
            }
            else -> {
                ListItemCardMainAddBinding.inflate(
                    layoutInflater, parent, false
                ).let {
                    AddViewHolder(it)
                }
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContentViewHolder -> holder.bind(getItem(position) as CardHomeItem.Content)
            is AddViewHolder -> holder.bind()
        }
    }

    inner class ContentViewHolder(
        private val binding: ListItemCardMainBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CardHomeItem.Content) {
            binding.txtCaption.text = item.caption

        }
    }

    inner class AddViewHolder(
        private val binding: ListItemCardMainAddBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.root.setOnClickListener {
                onAddClick()
            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<CardHomeItem>() {
            override fun areItemsTheSame(oldItem: CardHomeItem, newItem: CardHomeItem): Boolean {
                val content = oldItem is CardHomeItem.Content
                        && newItem is CardHomeItem.Content && oldItem.caption == newItem.caption

                val add = oldItem is CardHomeItem.Add
                        && newItem is CardHomeItem.Add && oldItem == newItem

                return content || add
            }


            override fun areContentsTheSame(oldItem: CardHomeItem, newItem: CardHomeItem): Boolean =
                oldItem == newItem
        }
    }
}