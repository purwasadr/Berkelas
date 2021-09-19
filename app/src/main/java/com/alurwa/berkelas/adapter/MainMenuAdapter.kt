package com.alurwa.berkelas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.berkelas.databinding.ListItemMenuMainBinding
import com.alurwa.berkelas.model.MainMenuItem

/**
 * Adapter for main menu
 */
class MainMenuAdapter(
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<MainMenuItem, MainMenuAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemMenuMainBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemMenuMainBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.txtCaption.text = getItem(position).name
            binding.root.setOnClickListener {
                onItemClick(position)
            }
        }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<MainMenuItem>() {
            override fun areItemsTheSame(oldMenuItem: MainMenuItem, newMenuItem: MainMenuItem): Boolean =
                oldMenuItem.name == newMenuItem.name

            override fun areContentsTheSame(oldMenuItem: MainMenuItem, newMenuItem: MainMenuItem): Boolean =
                oldMenuItem == newMenuItem

        }
    }
}