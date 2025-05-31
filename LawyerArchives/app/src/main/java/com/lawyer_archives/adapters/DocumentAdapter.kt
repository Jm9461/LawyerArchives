package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.databinding.ItemDocumentBinding
import com.lawyer_archives.models.Document

class DocumentAdapter(
    private val onDocumentClick: (Document) -> Unit,
    private val onDeleteClick: (Document) -> Unit
) : ListAdapter<Document, DocumentAdapter.DocumentViewHolder>(DocumentDiffCallback()) {

    // حالا DocumentViewHolder توابع کلیک را از سازنده دریافت می‌کند
    class DocumentViewHolder(
        private val binding: ItemDocumentBinding,
        private val onDocumentClick: (Document) -> Unit,
        private val onDeleteClick: (Document) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        // متد bind از توابع کلیک که در constructor دریافت شده‌اند استفاده می‌کند
        fun bind(item: Document) {
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
        // اینجا توابع کلیک را به سازنده DocumentViewHolder ارسال می‌کنیم
        return DocumentViewHolder(binding, onDocumentClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        // حالا متد bind فقط آیتم را نیاز دارد، زیرا توابع کلیک در ViewHolder ذخیره شده‌اند
        holder.bind(getItem(position))
    }

    class DocumentDiffCallback : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean =
            oldItem == newItem
    }
}