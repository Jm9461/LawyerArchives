package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.R
import com.lawyer_archives.adapters.CaseAdapter
import com.lawyer_archives.databinding.ActivityCasesBinding
import com.lawyer_archives.models.Case
import com.lawyer_archives.utils.XmlManager

/**
 * صفحه نمایش لیست تمام پرونده‌ها
 */
class CasesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCasesBinding
    private lateinit var adapter: CaseAdapter
    private val caseList = mutableListOf<Case>()

    private object Extras {
        const val CASE_ID = "extra_case_id"
        const val CASE_TITLE = "extra_case_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadCases()

        binding.addCaseButton.setOnClickListener {
            startActivity(Intent(this, AddCaseActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = CaseAdapter(
            onEditClick = { caseItem ->
                startActivity(
                    Intent(this, EditCaseActivity::class.java)
                        .putExtra(Extras.CASE_ID, caseItem.id)
                )
            },
            onDeleteClick = { caseItem ->
                confirmAndDeleteCase(caseItem)
            },
            onDocumentsClick = { caseItem ->
                startActivity(
                    Intent(this, CaseDocumentsActivity::class.java)
                        .putExtra(Extras.CASE_ID, caseItem.id)
                        .putExtra(Extras.CASE_TITLE, caseItem.title)
                )
            }
        )

        binding.casesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CasesActivity)
            adapter = this@CasesActivity.adapter
        }
    }

    private fun loadCases() {
        val loadedCases = XmlManager.loadCases(this)
        caseList.clear()
        caseList.addAll(loadedCases)
        adapter.updateList(caseList)

        if (caseList.isEmpty()) {
            Toast.makeText(this, R.string.no_cases_yet, Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmAndDeleteCase(caseItem: Case) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_case_title)
            .setMessage(getString(R.string.delete_case_message, caseItem.title))
            .setPositiveButton(R.string.yes) { _, _ -> deleteCase(caseItem) }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    private fun deleteCase(caseItem: Case) {
        caseList.remove(caseItem)
        XmlManager.saveCases(this, caseList)
        XmlManager.deleteDocumentsForCase(this, caseItem.id)
        Toast.makeText(this, R.string.case_deleted_successfully, Toast.LENGTH_SHORT).show()
        adapter.updateList(caseList)
    }

    override fun onResume() {
        super.onResume()
        loadCases()
    }
}