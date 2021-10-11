package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemRoomBinding
import com.alurwa.berkelas.model.RoomItem
import com.alurwa.common.model.RoomData

class RoomListAdapter(
    val onItemClick: (roomData: RoomData) -> Unit
) : ListAdapter<RoomItem, RoomListAdapter.ViewHolder>(COMPARATOR) {

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
            val data = getItem(position)
            val roomData = data.roomData

            binding.tvName.text = roomData.roomName
            binding.txtSchool.text = roomData.schoolName
            binding.imgCheck.isVisible = data.checked
            binding.root.setOnClickListener {
                onItemClick(roomData)
            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<RoomItem>() {
            override fun areItemsTheSame(oldItem: RoomItem, newItem: RoomItem): Boolean =
                oldItem.roomData.id == newItem.roomData.id

            override fun areContentsTheSame(oldItem: RoomItem, newItem: RoomItem): Boolean =
                oldItem == newItem

        }
    }
}