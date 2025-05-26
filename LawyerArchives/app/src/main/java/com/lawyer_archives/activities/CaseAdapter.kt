package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.databinding.ItemCaseBinding
import com.lawyer_archives.models.Case

class CaseAdapter(
    private val onEditClick: (Case) -> Unit,
    private val onDeleteClick: (Case) -> Unit
) : ListAdapter<Case, CaseAdapter.CaseViewHolder>(CaseDiffCallback()) {

    class CaseViewHolder(private val binding: ItemCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Case) {
            binding.title.text = item.title
            binding.date.text = "تاریخ: ${item.date}"

            binding.editButton.setOnClickListener {
                onEditClick(item)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCaseBinding.inflate(inflater, parent, false)
        return CaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CaseDiffCallback : DiffUtil.ItemCallback<Case>() {
        override fun areItemsTheSame(oldItem: Case, newItem: Case): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Case, newItem: Case): Boolean =
            oldItem == newItem
    }
}