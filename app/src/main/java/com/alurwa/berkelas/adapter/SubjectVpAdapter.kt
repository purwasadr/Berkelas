package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemSubjectBinding
import com.alurwa.berkelas.model.SubjectUiModel
import com.alurwa.common.model.Subject
import com.alurwa.common.model.SubjectItem

class SubjectVpAdapter(
    private val onSubjectItemClick: (subjectItem: SubjectItem) -> Unit
) : ListAdapter<Subject, SubjectVpAdapter.ViewHolder>(COMPARATOR) {

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

        private val adapter = SubjectListAdapter() {
            onSubjectItemClick(it)
        }

        init {
            with(binding) {
                listSubject.layoutManager = LinearLayoutManager(binding.root.context)
                listSubject.setHasFixedSize(true)
                listSubject.adapter = adapter
            }
        }

        fun bind(position: Int) {
            val item = getItem(position)

            val itemA = item.subjectItem.map {
                SubjectUiModel.Subject(
                    it
                )
            }
            val result = listOf(SubjectUiModel.Header(item.day ?: -1)) + itemA

            adapter.submitList(result)
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