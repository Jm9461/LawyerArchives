package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.databinding.ItemCourtSessionBinding // فرض کردم از ViewBinding استفاده می‌کنی (بهتره)
import com.lawyer_archives.models.CourtSession

class CourtSessionAdapter(
    private val onItemClick: (CourtSession) -> Unit
) : RecyclerView.Adapter<CourtSessionAdapter.ViewHolder>() {

    private var courtSessions: List<CourtSession> = emptyList()

    // استفاده از ViewBinding (خیلی تمیزتر و بدون findViewById)
    class ViewHolder(val binding: ItemCourtSessionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCourtSessionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = courtSessions[position]

        with(holder.binding) {
            tvSessionTitle.text = session.title.ifEmpty { "بدون عنوان" }
            
            // اینجا درست شد!
            tvSessionDate.text = session.courtDate
            tvSessionTime.text = session.courtTime
            tvSessionCourt.text = session.courtBranch

            root.setOnClickListener {
                onItemClick(session)
            }
        }
    }

    override fun getItemCount(): Int = courtSessions.size

    fun updateList(newList: List<CourtSession>) {
        courtSessions = newList
        notifyDataSetChanged()
    }
}