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

    // حالا CaseViewHolder توابع کلیک را از سازنده دریافت می‌کند
    class CaseViewHolder(
        private val binding: ItemCaseBinding,
        private val onEditClick: (Case) -> Unit,
        private val onDeleteClick: (Case) -> Unit,
        private val onDocumentsClick: (Case) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        // متد bind از توابع کلیک که در constructor دریافت شده‌اند استفاده می‌کند
        fun bind(item: Case) {
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

            binding.documentsButton.setOnClickListener {
                onDocumentsClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCaseBinding.inflate(inflater, parent, false)
        // اینجا توابع کلیک را به سازنده CaseViewHolder ارسال می‌کنیم
        return CaseViewHolder(binding, onEditClick, onDeleteClick, onDocumentsClick)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        // حالا متد bind فقط آیتم را نیاز دارد، زیرا توابع کلیک در ViewHolder ذخیره شده‌اند
        holder.bind(getItem(position))
    }

    class CaseDiffCallback : DiffUtil.ItemCallback<Case>() {
        override fun areItemsTheSame(oldItem: Case, newItem: Case): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Case, newItem: Case): Boolean =
            oldItem == newItem
    }
}