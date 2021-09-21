package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemSubjectBinding
import com.alurwa.common.model.SubjectItem

class SubjectListAdapter : ListAdapter<SubjectItem, SubjectListAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemSubjectBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemSubjectBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.txtSubject.text = getItem(position).subject
            binding.root.setOnClickListener {

            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<SubjectItem>() {
            override fun areItemsTheSame(
                oldItem: SubjectItem,
                newItem: SubjectItem
            ): Boolean =
                oldItem.subject == newItem.subject

            override fun areContentsTheSame(
                oldItem: SubjectItem,
                newItem: SubjectItem
            ): Boolean =
                oldItem == newItem

        }
    }
}