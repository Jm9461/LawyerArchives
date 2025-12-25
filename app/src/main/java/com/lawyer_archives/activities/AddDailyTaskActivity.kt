package com.lawyer_archives.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.R
import com.lawyer_archives.databinding.ActivityAddDailyTaskBinding
import com.lawyer_archives.helpers.DatabaseHelper
import com.lawyer_archives.helpers.SessionManager
import com.lawyer_archives.models.DailyTask
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

/**
 * صفحه اضافه کردن وظیفه روزانه جدید
 */
class AddDailyTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDailyTaskBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    // فرمت تاریخ و زمان رو به strings.xml منتقل کردیم
    private val dateFormatter by lazy { SimpleDateFormat(getString(R.string.date_format), Locale.getDefault()) }
    private val timeFormatter by lazy { SimpleDateFormat(getString(R.string.time_format), Locale.getDefault()) }

    /** تنظیمات اولیه صفحه */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDailyTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        binding.rbMediumPriority.isChecked = true
        binding.etTaskDueDate.setOnClickListener { showDatePicker() }
        binding.etTaskDueTime.setOnClickListener { showTimePicker() }
        binding.btnSaveTask.setOnClickListener { addTask() }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val calendar = Calendar.getInstance().apply { set(year, month, day) }
                binding.etTaskDueDate.setText(dateFormatter.format(calendar.time))
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val c = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                binding.etTaskDueTime.setText(timeFormatter.format(calendar.time))
            },
            c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun addTask() {
        val title = binding.etTaskTitle.text.toString().trim()
        val description = binding.etTaskDescription.text.toString().trim()
        val dueDate = binding.etTaskDueDate.text.toString().trim()
        val dueTime = binding.etTaskDueTime.text.toString().trim()

        val priority = when {
            binding.rbHighPriority.isChecked -> getString(R.string.priority_high)
            binding.rbMediumPriority.isChecked -> getString(R.string.priority_medium)
            binding.rbLowPriority.isChecked -> getString(R.string.priority_low)
            else -> getString(R.string.priority_medium)
        }

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty() || dueTime.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            return
        }

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val task = DailyTask(
            id = UUID.randomUUID().toString(),
            userId = sessionManager.getUserId().toString(),
            title = title,
            description = description,
            dueDate = dueDate,
            dueTime = dueTime,
            priority = priority,
            addedDate = dateFormatter.format(today.time),
            relatedClientOrCase = ""
        )

        if (dbHelper.insertDailyTask(task) > 0L) {
            Toast.makeText(this, R.string.task_added_successfully, Toast.LENGTH_SHORT).show()
            clearForm()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, R.string.error_saving_task, Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        binding.etTaskTitle.text?.clear()
        binding.etTaskDescription.text?.clear()
        binding.etTaskDueDate.text?.clear()
        binding.etTaskDueTime.text?.clear()
        binding.rbMediumPriority.isChecked = true
    }
}