package com.lawyer_archives.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.DailyTaskAdapter
import com.lawyer_archives.databinding.ActivityDailyTasksBinding
import com.lawyer_archives.helpers.DatabaseHelper
import com.lawyer_archives.helpers.SessionManager
import com.lawyer_archives.models.DailyTask
import android.widget.Toast

/**
 * Activity اصلی برای نمایش و مدیریت لیست وظایف روزانه کاربر.
 */
class DailyTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyTasksBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: DailyTaskAdapter
    private var dailyTasksList = mutableListOf<DailyTask>()

    // [Fix]: حذف public و String: رفع Redundant visibility modifier و Hardcoded string literal
    @Suppress("SpellCheckingInspection", "HardcodedText")
    companion object {
        /** کلید Intent برای ارسال شناسه وظیفه روزانه. */
        const val EXTRA_TASK_ID = "DAILY_TASK_ID"
    }

    /**
     * متد onCreate: این Activity را مقداردهی اولیه می‌کند.
     *
     * @param savedInstanceState اطلاعاتی که در صورت بازسازی Activity حفظ شده است.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        setupRecyclerView()
        loadDailyTasks()

        binding.fabAddDailyTask.setOnClickListener {
            val intent = Intent(this, AddDailyTaskActivity::class.java)
            startActivity(intent)
        }

        binding.toolbarDailyTasks.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        // نسخه جدید آداپتور — بدون dailyTasks و context
        adapter = DailyTaskAdapter(
            onEditClick = { task ->
                val intent = Intent(this, EditDailyTaskActivity::class.java).apply {
                    putExtra(EXTRA_TASK_ID, task.id)
                }
                startActivity(intent)
            },
            onDeleteClick = { task ->
                showDeleteConfirmationDialog(task)
            }
        )

        binding.dailyTasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DailyTasksActivity)
            adapter = this@DailyTasksActivity.adapter
        }
    }

    private fun loadDailyTasks() {
        val userId = sessionManager.getUserId()
        val tasks = databaseHelper.getDailyTasksByUserId(userId)

        dailyTasksList.clear()
        dailyTasksList.addAll(tasks)

        adapter.updateList(dailyTasksList)  // استفاده از متد جدید updateList

        if (dailyTasksList.isEmpty()) {
            binding.emptyStateLayout.visibility = android.view.View.VISIBLE
            binding.dailyTasksRecyclerView.visibility = android.view.View.GONE
        } else {
            binding.emptyStateLayout.visibility = android.view.View.GONE
            binding.dailyTasksRecyclerView.visibility = android.view.View.VISIBLE
        }
    }

    /**
     * متد onResume: هنگام بازگشت به Activity، لیست وظایف را مجدداً بارگذاری می‌کند.
     */
    override fun onResume() {
        super.onResume()
        loadDailyTasks()
    }

    private fun showDeleteConfirmationDialog(task: DailyTask) {
        AlertDialog.Builder(this)
            .setTitle("حذف وظیفه")
            .setMessage("آیا از حذف وظیفه \"${task.title}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteTask(task)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteTask(task: DailyTask) {
        dailyTasksList.remove(task)
        databaseHelper.deleteDailyTask(task.id)  // اگر متد حذف در DatabaseHelper داری
        adapter.updateList(dailyTasksList)
        Toast.makeText(this, "وظیفه با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()

        // بروزرسانی حالت خالی
        if (dailyTasksList.isEmpty()) {
            binding.emptyStateLayout.visibility = android.view.View.VISIBLE
            binding.dailyTasksRecyclerView.visibility = android.view.View.GONE
        }
    }
}