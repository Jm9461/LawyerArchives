// LawyerArchives/app/src/main/java/com/lawyer_archives/adapters/CaseAdapter.kt
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
    private val onDeleteClick: (Case) -> Unit,
    private val onDocumentsClick: (Case) -> Unit // New click listener for documents
) : ListAdapter<Case, CaseAdapter.CaseViewHolder>(CaseDiffCallback()) {

    class CaseViewHolder(private val binding: ItemCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Case, onEditClick: (Case) -> Unit, onDeleteClick: (Case) -> Unit, onDocumentsClick: (Case) -> Unit) {
            binding.title.text = item.title
            binding.description.text = item.description
            binding.clientName.text = "موکل: ${item.clientName}"
            binding.courtDate.text = "تاریخ دادگاه: ${item.courtDate}"
            binding.status.text = "وضعیت: ${item.status}"
            binding.addedDate.text = "تاریخ افزودن: ${item.addedDate}"

            binding.editButton.setOnClickListener {
                onEditClick(item)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(item)
            }

            binding.documentsButton.setOnClickListener { // New button click listener
                onDocumentsClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCaseBinding.inflate(inflater, parent, false)
        return CaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        holder.bind(getItem(position), onEditClick, onDeleteClick, onDocumentsClick)
    }

    class CaseDiffCallback : DiffUtil.ItemCallback<Case>() {
        override fun areItemsTheSame(oldItem: Case, newItem: Case): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Case, newItem: Case): Boolean =
            oldItem == newItem
    }
}