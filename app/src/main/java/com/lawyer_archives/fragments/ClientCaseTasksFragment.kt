// مسیر دقیق: app/src/main/java/com/lawyer_archives/fragments/ClientCaseTasksFragment.kt
package com.lawyer_archives.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.DailyTaskAdapter
import com.lawyer_archives.databinding.FragmentClientCaseTasksBinding
import com.lawyer_archives.models.DailyTask
import com.lawyer_archives.utils.XmlManager

class ClientCaseTasksFragment : Fragment() {

    // ViewBinding
    private var _binding: FragmentClientCaseTasksBinding? = null
    private val binding get() = _binding!!

    // آداپتور و لیست وظایف
    private lateinit var adapter: DailyTaskAdapter
    private val taskList = mutableListOf<DailyTask>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientCaseTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadTasks()
    }

    private fun setupRecyclerView() {
        // ساخت آداپتور با نسخه جدید (فقط دو لامبدا)
        adapter = DailyTaskAdapter(
            onEditClick = { task ->
                Toast.makeText(requireContext(), "ویرایش وظیفه پرونده: ${task.title}", Toast.LENGTH_SHORT).show()
                // بعداً اینجا به صفحه ویرایش برو
            },
            onDeleteClick = { task ->
                Toast.makeText(requireContext(), "حذف وظیفه پرونده: ${task.title}", Toast.LENGTH_SHORT).show()
                // بعداً اینجا تأیید حذف بزن
            }
        )

        binding.recyclerViewClientCaseTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ClientCaseTasksFragment.adapter
        }
    }

    private fun loadTasks() {
        // لود وظایف از XML
        val loadedTasks = XmlManager.loadDailyTasks(requireContext())

        taskList.clear()
        taskList.addAll(loadedTasks)

        // آپدیت لیست در آداپتور
        adapter.updateList(taskList)

        // پیام خالی بودن لیست
        if (taskList.isEmpty()) {
            Toast.makeText(requireContext(), "هیچ کار مرتبط با موکل یا پرونده ثبت نشده است.", Toast.LENGTH_LONG).show()
        }
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