package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ListItemPicketBinding
import com.alurwa.berkelas.databinding.ListItemSubjectEmptyBinding
import com.alurwa.berkelas.databinding.ListItemSubjectHeaderBinding
import com.alurwa.berkelas.model.HeaderDay
import com.alurwa.berkelas.model.ListItem
import com.alurwa.berkelas.model.PicketEmpty
import com.alurwa.berkelas.model.PicketUi

class PicketAdapter(
    private val onClick: (picket: PicketUi) -> Unit
) : ListAdapter<ListItem, RecyclerView.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.list_item_subject_header -> {
                ListItemSubjectHeaderBinding.inflate(
                    layoutInflater, parent, false
                ).let {
                    HeaderViewHolder(it)
                }
            }
            R.layout.list_item_subject_empty -> {
                ListItemSubjectEmptyBinding.inflate(
                    layoutInflater, parent, false
                ).let {
                    EmptyViewHolder(it)
                }
            }
            else -> {
                ListItemPicketBinding.inflate(
                    layoutInflater, parent, false
                ).let {
                    PicketViewHolder(it)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(position)
            is EmptyViewHolder -> holder.bind(position)
            is PicketViewHolder -> holder.bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HeaderDay -> R.layout.list_item_subject_header
        is PicketEmpty -> R.layout.list_item_subject_empty
        is PicketUi -> R.layout.list_item_picket
        else -> throw IllegalStateException("Unknown view")
    }

    inner class HeaderViewHolder(
        private val binding: ListItemSubjectHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position) as HeaderDay
            binding.day = item.day

            binding.executePendingBindings()
        }
    }

    inner class PicketViewHolder(
        private val binding: ListItemPicketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position) as PicketUi

            binding.picket = item

            binding.root.setOnClickListener {
                onClick(item)
            }
            binding.executePendingBindings()
        }
    }

    inner class EmptyViewHolder(
        private val binding: ListItemSubjectEmptyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position) as PicketEmpty

            binding.day = item.day
            binding.emptyText = "No picket"
            binding.emptyImg = R.drawable.ic_round_class_24
            binding.executePendingBindings()
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                val subject = oldItem is PicketUi
                        && newItem is PicketUi && oldItem.id == newItem.id

                val empty = oldItem is PicketEmpty
                        && newItem is PicketEmpty && oldItem.day == newItem.day

                val header = oldItem is HeaderDay
                        && newItem is HeaderDay && oldItem.day == newItem.day

                return subject || header || empty
            }

            override fun areContentsTheSame(
                oldItem: ListItem,
                newItem: ListItem
            ): Boolean =
                oldItem == newItem
        }
    }
}