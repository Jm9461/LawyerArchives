package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.R
import com.lawyer_archives.models.Meeting

/**
 * آداپتور برای نمایش لیست جلسات
 * نسخه جدید: بدون meetings و context در سازنده
 */
class MeetingAdapter(
    private val onEditClick: (Meeting) -> Unit,
    private val onDeleteClick: (Meeting) -> Unit
) : RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    // لیست داخلی برای آپدیت شدن
    private var meetings: List<Meeting> = emptyList()

    class MeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val meetingTopic: TextView = itemView.findViewById(R.id.meetingTopic)
        val meetingDate: TextView = itemView.findViewById(R.id.meetingDate)
        val meetingClientName: TextView = itemView.findViewById(R.id.meetingClientName)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meeting, parent, false)
        return MeetingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = meetings[position]

        holder.meetingTopic.text = meeting.title
        holder.meetingDate.text = "تاریخ: ${meeting.date}"
        holder.meetingClientName.text = "موکل: ${meeting.clientName}"

        holder.editButton.setOnClickListener { onEditClick(meeting) }
        holder.deleteButton.setOnClickListener { onDeleteClick(meeting) }
    }

    override fun getItemCount() = meetings.size

    // متد مهم برای آپدیت لیست
    fun updateList(newList: List<Meeting>) {
        meetings = newList
        notifyDataSetChanged()
    }
}