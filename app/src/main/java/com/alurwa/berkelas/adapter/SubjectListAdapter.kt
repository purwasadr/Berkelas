package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemSubjectItemBinding
import com.alurwa.common.model.SubjectItem

class SubjectListAdapter(
    private val onClick: (subjectItem: SubjectItem) -> Unit
) : ListAdapter<SubjectItem, SubjectListAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemSubjectItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemSubjectItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position)
            with(binding) {
                txtSubject.text = item.subject
                txtTime.text = "${item.startTime} - ${item.endTime}"
                root.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<SubjectItem>() {
            override fun areItemsTheSame(
                oldItem: SubjectItem,
                newItem: SubjectItem
            ): Boolean =
               oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SubjectItem,
                newItem: SubjectItem
            ): Boolean =
                oldItem == newItem

        }
    }
}