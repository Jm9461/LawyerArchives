// LawyerArchives/app/src/main/java/com/lawyer_archives/activities/CasesActivity.kt
package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.adapters.CaseAdapter
import com.lawyer_archives.databinding.ActivityCasesBinding
import com.lawyer_archives.models.Case
import com.lawyer_archives.utils.XmlManager
import java.util.UUID
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CasesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCasesBinding
    private lateinit var caseList: MutableList<Case>
    private lateinit var adapter: CaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadCases()

        binding.addCaseButton.setOnClickListener {
            val intent = Intent(this, AddCaseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = CaseAdapter(
            onEditClick = { case ->
                val intent = Intent(this, EditCaseActivity::class.java).apply {
                    putExtra("caseId", case.id)
                }
                startActivity(intent)
            },
            onDeleteClick = { case ->
                confirmAndDeleteCase(case)
            },
            onDocumentsClick = { case -> // New click handler for documents
                val intent = Intent(this, CaseDocumentsActivity::class.java).apply {
                    putExtra("caseId", case.id)
                    putExtra("caseTitle", case.title) // Pass title for display
                }
                startActivity(intent)
            }
        )
        binding.casesRecyclerView.adapter = adapter
    }

    private fun loadCases() {
        caseList = XmlManager.loadCases(this)
        adapter.submitList(caseList.toList())
        if (caseList.isEmpty()) {
            Toast.makeText(this, "هنوز پرونده‌ای ثبت نشده است.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmAndDeleteCase(case: Case) {
        AlertDialog.Builder(this)
            .setTitle("حذف پرونده")
            .setMessage("آیا از حذف پرونده \"${case.title}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteCase(case)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteCase(case: Case) {
        caseList.remove(case)
        XmlManager.saveCases(this, caseList)
        adapter.submitList(caseList.toList())
        Toast.makeText(this, "پرونده با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadCases() // Refresh list when returning to activity
    }
}