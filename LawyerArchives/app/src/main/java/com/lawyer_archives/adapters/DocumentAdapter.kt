// LawyerArchives/app/src/main/java/com/lawyer_archives/adapters/DocumentAdapter.kt
package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.databinding.ItemDocumentBinding // We'll create this layout
import com.lawyer_archives.models.Document

class DocumentAdapter(
    private val onDocumentClick: (Document) -> Unit,
    private val onDeleteClick: (Document) -> Unit
) : ListAdapter<Document, DocumentAdapter.DocumentViewHolder>(DocumentDiffCallback()) {

    class DocumentViewHolder(private val binding: ItemDocumentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Document, onDocumentClick: (Document) -> Unit, onDeleteClick: (Document) -> Unit) {
            binding.documentTitle.text = item.title
            binding.documentDate.text = "تاریخ افزودن: ${item.addedDate}"
            binding.documentExtension.text = "نوع: ${item.fileExtension.uppercase()}"

            binding.root.setOnClickListener {
                onDocumentClick(item)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDocumentBinding.inflate(inflater, parent, false)
        return DocumentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.bind(getItem(position), onDocumentClick, onDeleteClick)
    }

    class DocumentDiffCallback : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean =
            oldItem == newItem
    }
}