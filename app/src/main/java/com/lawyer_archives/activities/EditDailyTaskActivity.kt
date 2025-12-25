package com.lawyer_archives.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.R
import com.lawyer_archives.databinding.ActivityEditDailyTaskBinding
import com.lawyer_archives.helpers.DatabaseHelper
import com.lawyer_archives.models.DailyTask
import java.util.Calendar
import java.util.Locale

/**
 * صفحه ویرایش وظیفه روزانه
 */
class EditDailyTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDailyTaskBinding
    private lateinit var databaseHelper: DatabaseHelper

    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var taskId: String = ""

    companion object {
        /** کلید Intent برای دریافت شناسه وظیفه */
        private val EXTRA_TASK_ID = "DAILY_TASK_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDailyTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

       taskId = intent.getStringExtra(EXTRA_TASK_ID).orEmpty()
        if (taskId.isEmpty()) {
            Toast.makeText(this, getString(R.string.task_not_found), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.edit_daily_task_title)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupUI()
    }

    private fun setupUI() {
        loadTaskData()
        setupTaskTypeDropdown()
        setupDatePicker()
        setupTimePicker()
        setupButtons()
    }

    private fun loadTaskData() {
        val task = databaseHelper.getDailyTaskById(taskId) ?: run {
            Toast.makeText(this, getString(R.string.error_loading_task), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        (binding.actEditDailyTaskAutoCompleteTaskType.editText as? AutoCompleteTextView)
            ?.setText(task.title, false)

        binding.actEditDailyTaskDescription.editText?.setText(task.description)

        selectedDate = task.dueDate
        selectedTime = task.dueTime

        binding.actEditDailyTaskBtnSelectDate.text = if (task.dueDate.isNotEmpty()) task.dueDate else getString(R.string.select_date)
        binding.actEditDailyTaskBtnSelectTime.text = if (task.dueTime.isNotEmpty()) task.dueTime else getString(R.string.select_time)
    }

    private fun setupTaskTypeDropdown() {
        val types = resources.getStringArray(R.array.task_type_array)
        val adapter = ArrayAdapter(this, R.layout.dropdown_menu_item, types)
        (binding.actEditDailyTaskAutoCompleteTaskType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    @SuppressLint("SetTextI18n")
    private fun setupDatePicker() {
        binding.actEditDailyTaskBtnSelectDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            if (selectedDate.isNotEmpty()) {
                try {
                    val parts = selectedDate.split("/")
                    if (parts.size == 3) {
                        c.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
                    }
                } catch (ignore: Exception) {}
            }

            DatePickerDialog(this, { _, y, m, d ->
                selectedDate = String.format(Locale.getDefault(), "%04d/%02d/%02d", y, m + 1, d)
                binding.actEditDailyTaskBtnSelectDate.text = selectedDate
            }, year, month, day).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupTimePicker() {
        binding.actEditDailyTaskBtnSelectTime.setOnClickListener {
            val c = Calendar.getInstance()
            var hour = c.get(Calendar.HOUR_OF_DAY)
            var minute = c.get(Calendar.MINUTE)

            if (selectedTime.isNotEmpty()) {
                try {
                    val parts = selectedTime.split(":")
                    hour = parts[0].toInt()
                    minute = parts[1].toInt()
                } catch (ignore: Exception) {}
            }

            TimePickerDialog(this, { _, h, m ->
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", h, m)
                binding.actEditDailyTaskBtnSelectTime.text = selectedTime
            }, hour, minute, true).show()
        }
    }

    private fun setupButtons() {
        binding.actEditDailyTaskBtnUpdate.setOnClickListener { updateTask() }
        binding.actEditDailyTaskBtnDelete.setOnClickListener { deleteTask() }
    }

    private fun updateTask() {
        val title = binding.actEditDailyTaskAutoCompleteTaskType.editText?.text.toString().trim()
        val description = binding.actEditDailyTaskDescription.editText?.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val updatedTask = DailyTask(
            id = taskId,
            title = title,
            description = description,
            dueDate = selectedDate,
            dueTime = selectedTime,
            priority = "",
            isCompleted = false,
            addedDate = ""
        )

        if (databaseHelper.updateDailyTask(updatedTask) > 0) {
            Toast.makeText(this, getString(R.string.task_updated_successfully), Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.error_updating_task), Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteTask() {
        if (databaseHelper.deleteDailyTask(taskId) > 0) {
            Toast.makeText(this, getString(R.string.task_deleted_successfully), Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.error_deleting_task), Toast.LENGTH_SHORT).show()
        }
    }
}