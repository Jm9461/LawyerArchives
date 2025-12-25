package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.databinding.ItemCaseBinding
import com.lawyer_archives.models.Case

/**
 * آداپتور نمایش لیست پرونده‌ها
 */
class CaseAdapter(
    private val onEditClick: (Case) -> Unit,
    private val onDeleteClick: (Case) -> Unit,
    private val onDocumentsClick: (Case) -> Unit
) : RecyclerView.Adapter<CaseAdapter.ViewHolder>() {

    private val cases = mutableListOf<Case>()

    class ViewHolder(val binding: ItemCaseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val caseItem = cases[position]
        with(holder.binding) {
            itemCaseTvTitle.text = caseItem.title
            itemCaseTvCaseNumber.text = "شماره پرونده: ${caseItem.caseNumber}"
            itemCaseTvCaseType.text = "نوع: ${caseItem.caseType}"
            itemCaseTvCourtName.text = "دادگاه: ${caseItem.courtName}"
            itemCaseTvCourtLocation.text = "محل: ${caseItem.courtLocation}"
            itemCaseTvCaseStatus.text = "وضعیت: ${caseItem.status}"

            itemCaseBtnEdit.setOnClickListener { onEditClick(caseItem) }
            itemCaseBtnDelete.setOnClickListener { onDeleteClick(caseItem) }
            itemCaseBtnDocuments.setOnClickListener { onDocumentsClick(caseItem) }
        }
    }

    override fun getItemCount() = cases.size

    /** بروزرسانی لیست پرونده‌ها */
    fun updateList(newList: List<Case>) {
        cases.clear()
        cases.addAll(newList)
        notifyDataSetChanged()
    }
}