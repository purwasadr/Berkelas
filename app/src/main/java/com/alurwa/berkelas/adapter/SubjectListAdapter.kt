package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ListItemSubjectEmptyBinding
import com.alurwa.berkelas.databinding.ListItemSubjectHeaderBinding
import com.alurwa.berkelas.databinding.ListItemSubjectItemBinding
import com.alurwa.berkelas.model.SubjectUiModel
import com.alurwa.common.model.SubjectItem

class SubjectListAdapter(
    private val onClick: (subjectItem: SubjectItem) -> Unit
) : ListAdapter<SubjectUiModel, RecyclerView.ViewHolder>(COMPARATOR) {
    override fun submitList(list: List<SubjectUiModel>?) {
        if (list == currentList) return
        super.submitList(list)
    }

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
                ListItemSubjectItemBinding.inflate(
                    layoutInflater, parent, false
                ).let {
                    SubjectViewHolder(it)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(position)
            is SubjectViewHolder -> holder.bind(position)
            is EmptyViewHolder -> holder.bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is SubjectUiModel.Header -> R.layout.list_item_subject_header
        is SubjectUiModel.Subject -> R.layout.list_item_subject_item
        is SubjectUiModel.Empty -> R.layout.list_item_subject_empty
        else -> throw IllegalStateException("Unknown view")
    }

    inner class HeaderViewHolder(
        private val binding: ListItemSubjectHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position) as SubjectUiModel.Header
            binding.day = item.day
        }
    }

    inner class SubjectViewHolder(
        private val binding: ListItemSubjectItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position) as SubjectUiModel.Subject

            binding.subjectItem = item.subject

            binding.root.setOnClickListener {
                onClick(item.subject)
            }

            binding.executePendingBindings()
        }
    }

    inner class EmptyViewHolder(
        private val binding: ListItemSubjectEmptyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position) as SubjectUiModel.Empty

            binding.day = item.day
            binding.emptyText = binding.root.context.getString(R.string.tv_no_subject)
            binding.emptyImg = R.drawable.ic_round_class_24
            binding.executePendingBindings()
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<SubjectUiModel>() {
            override fun areItemsTheSame(
                oldItem: SubjectUiModel,
                newItem: SubjectUiModel
            ): Boolean {
                val subject = oldItem is SubjectUiModel.Subject
                        && newItem is SubjectUiModel.Subject && oldItem.subject.id == newItem.subject.id

                val header = oldItem is SubjectUiModel.Header
                        && newItem is SubjectUiModel.Header && oldItem.day == newItem.day

                val empty = oldItem is SubjectUiModel.Empty
                        && newItem is SubjectUiModel.Empty && oldItem == newItem

                return subject || header || empty
            }

            override fun areContentsTheSame(
                oldItem: SubjectUiModel,
                newItem: SubjectUiModel
            ): Boolean =
                oldItem == newItem

        }
    }
}