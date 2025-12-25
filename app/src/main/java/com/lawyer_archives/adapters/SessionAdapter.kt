package com.lawyer_archives.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.R
import com.lawyer_archives.models.CourtSession

/**
 * آداپتور برای نمایش لیست جلسات دادگاه در RecyclerView.
 *
 * @property sessions لیست فعلی جلسات دادگاه.
 * @property context کانتکست (Context) برای دسترسی به منابع.
 * @property onEditClick تابع کال‌بک برای مدیریت کلیک بر روی دکمه ویرایش.
 * @property onDeleteClick تابع کال‌بک برای مدیریت کلیک بر روی دکمه حذف.
 */
class SessionAdapter(
    private var sessions: List<CourtSession>,
    private val context: Context,
    private val onEditClick: (CourtSession) -> Unit,
    private val onDeleteClick: (CourtSession) -> Unit
) : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    /**
     * ViewHolder برای نگهداری و مدیریت Viewهای یک آیتم در لیست.
     *
     * @param itemView نمای کلی (View) مربوط به یک سطر لیست.
     */
    class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** عنوان جلسه دادگاه. */
        val sessionTitle: TextView = itemView.findViewById(R.id.sessionTitle)

        /** تاریخ جلسه دادگاه. */
        val sessionDate: TextView = itemView.findViewById(R.id.sessionDate)

        /** محل برگزاری جلسه دادگاه. */
        val location: TextView = itemView.findViewById(R.id.location)

        /** دکمه ویرایش جلسه. */
        val editButton: Button = itemView.findViewById(R.id.editButton)

        /** دکمه حذف جلسه. */
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    /**
     * یک ViewHolder جدید ایجاد می‌کند.
     *
     * @param parent ویوگروپ پدر.
     * @param viewType نوع ویو (در اینجا استفاده نمی‌شود).
     * @return یک نمونه جدید از SessionViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_session, parent, false)
        return SessionViewHolder(view)
    }

    /**
     * داده‌ها را به Viewهای داخل ViewHolder متصل می‌کند.
     *
     * @param holder نمونه ViewHolder مربوطه.
     * @param position موقعیت آیتم در لیست.
     */
    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]

        holder.sessionTitle.text = session.title
        holder.sessionDate.text = session.courtDate
        holder.location.text = session.location

        holder.editButton.setOnClickListener {
            onEditClick(session)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(session)
        }
    }

    /**
     * تعداد کل آیتم‌ها را برمی‌گرداند.
     *
     * @return تعداد آیتم‌های لیست.
     */
    override fun getItemCount(): Int = sessions.size

    /**
     * لیست جدید جلسات را جایگزین و RecyclerView را به‌روزرسانی می‌کند.
     * هشدار NotifyDataSetChanged با انوتیشن SuppressLint مدیریت شده است.
     *
     * @param newSessions لیست جدید جلسات.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newSessions: List<CourtSession>) {
        sessions = newSessions
        notifyDataSetChanged()
    }
}