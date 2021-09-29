package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemRoomBinding
import com.alurwa.common.model.RoomData

class RoomListAdapter(
    val onItemClick: (roomData: RoomData) -> Unit
) : ListAdapter<RoomData, RoomListAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemRoomBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemRoomBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val data = getItem(position) ?: RoomData.EMPTY

            binding.txtName.text = data.roomName
            binding.txtSchool.text = data.schoolName
            binding.root.setOnClickListener {
                onItemClick(data)
            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<RoomData>() {
            override fun areItemsTheSame(oldItem: RoomData, newItem: RoomData): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: RoomData, newItem: RoomData): Boolean =
                oldItem == newItem

        }
    }
}