// مسیر: app/src/main/java/com/lawyer_archives/fragments/DailyTasksFragment.kt
package com.lawyer_archives.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.DailyTaskAdapter
import com.lawyer_archives.databinding.FragmentDailyTasksBinding
import com.lawyer_archives.models.DailyTask
import com.lawyer_archives.utils.XmlManager

class DailyTasksFragment : Fragment() {

    private var _binding: FragmentDailyTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: DailyTaskAdapter
    private val taskList = mutableListOf<DailyTask>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadTasks()
    }

    private fun setupRecyclerView() {
        // نسخه جدید آداپتور (فقط دو لامبدا)
        taskAdapter = DailyTaskAdapter(
            onEditClick = { task ->
                Toast.makeText(requireContext(), "ویرایش وظیفه: ${task.title}", Toast.LENGTH_SHORT).show()
                // بعداً به صفحه ویرایش برو
            },
            onDeleteClick = { task ->
                confirmAndDelete(task)
            }
        )

        // اسم درست از XML: recyclerViewDailyTasks
        binding.recyclerViewDailyTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    private fun loadTasks() {
        val tasks = XmlManager.loadDailyTasks(requireContext())

        taskList.clear()
        taskList.addAll(tasks)

        // آپدیت لیست با متد جدید
        taskAdapter.updateList(taskList)

        if (taskList.isEmpty()) {
            Toast.makeText(requireContext(), "هیچ وظیفه روزانه‌ای ثبت نشده است.", Toast.LENGTH_LONG).show()
        }
    }

    private fun confirmAndDelete(task: DailyTask) {
        // بعداً دیالوگ حذف رو اینجا بزن
        Toast.makeText(requireContext(), "حذف وظیفه: ${task.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadTasks() // هر بار که برگشت، لیست رفرش بشه
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}