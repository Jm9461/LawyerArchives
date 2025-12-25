package com.lawyer_archives.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.R
import com.lawyer_archives.activities.EditMeetingActivity
import com.lawyer_archives.models.Event
import com.lawyer_archives.models.Meeting

class EventAdapter(
    private val context: Context
) : ListAdapter<Any, EventAdapter.EventViewHolder>(EventDiffCallback()) {  // Any به جای Event

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEventType: TextView = itemView.findViewById(R.id.tvEventType)
        val tvEventTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
        val tvEventDetails: TextView = itemView.findViewById(R.id.tvEventDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = getItem(position)

        with(holder) {
            when (item) {
                is Meeting -> {
                    tvEventType.text = "جلسه"
                    tvEventTitle.text = item.title
                    tvEventDetails.text = "تاریخ: ${item.date} | ساعت: ${item.time} | موکل: ${item.clientName}"

                    itemView.setOnClickListener {
                        val intent = Intent(context, EditMeetingActivity::class.java).apply {
                            putExtra("EVENT_ID", item.id)  // id از نوع String
                            putExtra("IS_MEETING", true)
                        }
                        context.startActivity(intent)
                    }
                }

                is Event -> {
                    tvEventType.text = "رویداد"
                    tvEventTitle.text = item.title
                    tvEventDetails.text = "تاریخ: ${item.date} | ساعت: ${item.time}"

                    itemView.setOnClickListener {
                        // اگر بعداً صفحه ویرایش Event ساختی، اینجا بذار
                        // فعلاً فقط Toast یا چیزی
                    }
                }

                else -> {
                    tvEventTitle.text = "نامشخص"
                    tvEventDetails.text = ""
                }
            }
        }
    }
}

// DiffUtil برای Any (با id به عنوان شناسه منحصر به فرد)
class EventDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is Meeting && newItem is Meeting -> oldItem.id == newItem.id
            oldItem is Event && newItem is Event -> oldItem.id == newItem.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }
}