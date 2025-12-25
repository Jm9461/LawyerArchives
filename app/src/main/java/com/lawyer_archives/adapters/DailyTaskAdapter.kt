package com.lawyer_archives.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.R
import com.lawyer_archives.models.DailyTask

/**
 * آداپتور برای نمایش لیست وظایف روزانه در RecyclerView.
 *
 * @property onEditClick تابع کال‌بک برای مدیریت کلیک بر روی دکمه ویرایش.
 * @property onDeleteClick تابع کال‌بک برای مدیریت کلیک بر روی دکمه حذف.
 */
class DailyTaskAdapter(
    private val onEditClick: (DailyTask) -> Unit,
    private val onDeleteClick: (DailyTask) -> Unit
) : RecyclerView.Adapter<DailyTaskAdapter.DailyTaskViewHolder>() {

    // لیست داخلی که بعداً آپدیت میشه
    private var tasks: List<DailyTask> = emptyList()

    /**
     * ViewHolder برای نگهداری و مدیریت Viewهای یک آیتم در لیست.
     */
    class DailyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** عنوان وظیفه. (ID: tv_task_title) */
        val taskTitle: TextView = itemView.findViewById(R.id.tv_task_title)

        /** توضیحات وظیفه. (ID: tv_task_description) */
        val taskDescription: TextView = itemView.findViewById(R.id.tv_task_description)

        /** تاریخ مهلت انجام وظیفه. (ID: tv_task_due_date) */
        val taskDueDate: TextView = itemView.findViewById(R.id.tv_task_due_date)

        /** دکمه ویرایش وظیفه. */
        val editButton: Button = itemView.findViewById(R.id.btn_edit_task)

        /** دکمه حذف وظیفه. */
        val deleteButton: Button = itemView.findViewById(R.id.btn_delete_task)
    }

    /**
     * یک ViewHolder جدید ایجاد می‌کند.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_task, parent, false)
        return DailyTaskViewHolder(view)
    }

    /**
     * داده‌ها را به Viewهای داخل ViewHolder متصل می‌کند.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DailyTaskViewHolder, position: Int) {
        val dailyTask = tasks[position]

        holder.taskTitle.text = dailyTask.title
        holder.taskDescription.text = dailyTask.description

        // اینجا context رو دیگه لازم نداریم، ولی اگه string resource داشتی، می‌تونی از parent.context استفاده کنی
        // فعلاً ساده نگه داشتم، ولی اگه task_due_date_format داشتی، اینطوری بنویس:
        holder.taskDueDate.text = "مهلت: ${dailyTask.dueDate}"
        // یا اگه می‌خوای از string resource استفاده کنی:
        // holder.taskDueDate.text = holder.itemView.context.getString(R.string.task_due_date_format, dailyTask.dueDate)

        holder.editButton.setOnClickListener {
            onEditClick(dailyTask)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(dailyTask)
        }

        holder.itemView.setOnClickListener {
            onEditClick(dailyTask)
        }
    }

    /**
     * تعداد کل آیتم‌ها را برمی‌گرداند.
     */
    override fun getItemCount(): Int = tasks.size

    /**
     * متد مهم برای آپدیت لیست (این همون چیزیه که تو ClientCaseTasksFragment نیاز داری)
     */
    fun updateList(newList: List<DailyTask>) {
        tasks = newList
        notifyDataSetChanged()
    }
}