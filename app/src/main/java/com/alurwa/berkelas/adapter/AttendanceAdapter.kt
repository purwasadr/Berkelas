package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemAttendanceBinding
import com.alurwa.berkelas.model.AttendanceItem

class AttendanceAdapter: ListAdapter<AttendanceItem, AttendanceAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemAttendanceBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ListItemAttendanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AttendanceItem) {
            binding.attendanceItem = item
            binding.executePendingBindings()
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<AttendanceItem>() {
            override fun areItemsTheSame(
                oldItem: AttendanceItem,
                newItem: AttendanceItem
            ): Boolean =
                oldItem.date == oldItem.date

            override fun areContentsTheSame(
                oldItem: AttendanceItem,
                newItem: AttendanceItem
            ): Boolean =
                oldItem == oldItem

        }
    }
}