package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemPicketDayBinding
import com.alurwa.berkelas.model.HeaderDay
import com.alurwa.berkelas.model.PicketDayUi
import com.alurwa.berkelas.model.PicketEmpty
import com.alurwa.berkelas.model.PicketUi

class PicketDayAdapter(
    private val onPicketItemClick: (picket: PicketUi) -> Unit
) : ListAdapter<PicketDayUi, PicketDayAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemPicketDayBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemPicketDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter = PicketAdapter() {
            onPicketItemClick(it)
        }

        init {
            with(binding) {
                listPicket.layoutManager = LinearLayoutManager(binding.root.context)
                listPicket.setHasFixedSize(true)
                listPicket.adapter = adapter
            }
        }

        fun bind(position: Int) {
            val item = getItem(position)

//            val picketItem = item.pickets.map {
//                ListItem.PicketItem(
//                    it
//                )
//            }
//            val result = listOf(ListItem.Header(item.day)) + picketItem

            val transform = item.picketsUi.let {
                if (it.isEmpty()) {
                    listOf(PicketEmpty(item.day))
                } else {
                    listOf(HeaderDay(item.day)) +  it
                }
            }

            adapter.submitList(transform)
        }

    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<PicketDayUi>() {
            override fun areItemsTheSame(oldItem: PicketDayUi, newItem: PicketDayUi): Boolean =
                oldItem.day == newItem.day

            override fun areContentsTheSame(oldItem: PicketDayUi, newItem: PicketDayUi): Boolean =
                oldItem == newItem

        }
    }
}