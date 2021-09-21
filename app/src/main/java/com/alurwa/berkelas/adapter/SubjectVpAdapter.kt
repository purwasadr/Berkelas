package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.VpItemSubjectBinding
import com.alurwa.common.model.Subject

class SubjectVpAdapter
    : ListAdapter<Subject, SubjectVpAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            VpItemSubjectBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: VpItemSubjectBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter = SubjectListAdapter()

        init {
            with(binding) {
                listSubject.setHasFixedSize(true)
                listSubject.layoutManager = LinearLayoutManager(binding.root.context)
                listSubject.adapter = adapter
            }

        }

        fun bind(position: Int) {
            val item = getItem(position)

            if (item != null) {
                binding.txtDay.text = item.day.toString()
                adapter.submitList(item.subjectItem)
            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<Subject>() {
            override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean =
                oldItem.day == newItem.day

            override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean =
                oldItem == newItem

        }
    }
}