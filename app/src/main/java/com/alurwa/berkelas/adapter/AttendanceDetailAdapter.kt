package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemAttendanceDetailBinding
import com.alurwa.berkelas.model.AttendanceDetailItem

class AttendanceDetailAdapter: ListAdapter<AttendanceDetailItem, AttendanceDetailAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemAttendanceDetailBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ListItemAttendanceDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AttendanceDetailItem) {
            binding.attendanceDetailItem = item
            binding.executePendingBindings()
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<AttendanceDetailItem>() {
            override fun areItemsTheSame(
                oldItem: AttendanceDetailItem,
                newItem: AttendanceDetailItem
            ): Boolean =
                oldItem.userId == newItem.userId

            override fun areContentsTheSame(
                oldItem: AttendanceDetailItem,
                newItem: AttendanceDetailItem
            ): Boolean =
                oldItem == newItem

        }
    }
}