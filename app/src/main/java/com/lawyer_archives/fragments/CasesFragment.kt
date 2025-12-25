package com.lawyer_archives.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.activities.AddCaseActivity
import com.lawyer_archives.activities.EditCaseActivity
import com.lawyer_archives.adapters.CaseAdapter
import com.lawyer_archives.databinding.FragmentCasesBinding
import com.lawyer_archives.models.Case
import com.lawyer_archives.utils.XmlManager

class CasesFragment : Fragment() {

    private var _binding: FragmentCasesBinding? = null
    private val binding get() = _binding!!
    private lateinit var caseList: MutableList<Case>
    private lateinit var adapter: CaseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadCases()

        binding.fabAddCase.setOnClickListener {
            startActivity(Intent(requireContext(), AddCaseActivity::class.java))
        }

        // کامنت کردن بخش جستجو تا زمانی که ID صحیح پیدا شود
        /*
        binding.ivSearch.setOnClickListener {
            Toast.makeText(requireContext(), "جستجوی پرونده‌ها", Toast.LENGTH_SHORT).show()
        }
        */
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCases.layoutManager = LinearLayoutManager(requireContext())
        adapter = CaseAdapter(
            onEditClick = { case: Case ->
                val intent = Intent(requireContext(), EditCaseActivity::class.java).apply {
                    putExtra("caseId", case.id)
                }
                startActivity(intent)
            },
            onDeleteClick = { case: Case ->
                confirmAndDeleteCase(case)
            },
            onDocumentsClick = { case: Case ->
                Toast.makeText(requireContext(), "اسناد پرونده ${case.title}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerViewCases.adapter = adapter
    }

     private fun loadCases() {
         caseList = XmlManager.loadCases(requireContext()).toMutableList()
    
    // چون CaseAdapter احتمالاً هنوز ListAdapter نیست → از updateList استفاده کن
    adapter.updateList(caseList.toList())
    
    if (caseList.isEmpty()) {
        Toast.makeText(requireContext(), "هنوز پرونده‌ای ثبت نشده است.", Toast.LENGTH_SHORT).show()
    }
}

    private fun confirmAndDeleteCase(case: Case) {
        AlertDialog.Builder(requireContext())
            .setTitle("حذف پرونده")
            .setMessage("آیا از حذف پرونده \"${case.title}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteCase(case)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteCase(case: Case) {
        val initialSize = caseList.size
        caseList.remove(case)
        if (caseList.size < initialSize) {
            XmlManager.saveCases(requireContext(), caseList)
            XmlManager.deleteDocumentsForCase(requireContext(), case.id)
            Toast.makeText(requireContext(), "پرونده با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "خطا در حذف پرونده.", Toast.LENGTH_SHORT).show()
        }
        loadCases()
    }

    override fun onResume() {
        super.onResume()
        loadCases()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}