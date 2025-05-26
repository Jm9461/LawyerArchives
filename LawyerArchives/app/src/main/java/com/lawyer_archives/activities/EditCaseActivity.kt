package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditCaseBinding
import com.lawyer_archives.models.Case
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast

class EditCaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCaseBinding
    private var caseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        caseId = intent.getStringExtra("caseId")
        loadCaseData()

        binding.saveCaseButton.setOnClickListener {
            saveCase()
        }
    }

    private fun loadCaseData() {
        caseId?.let { id ->
            val caseToEdit = XmlManager.loadCases(this).find { it.id == id }
            caseToEdit?.let {
                binding.editTitle.setText(it.title)
                binding.editDescription.setText(it.description)
                binding.editClientName.setText(it.clientName)
                binding.editCourtDate.setText(it.courtDate)
                binding.editStatus.setText(it.status)
                binding.editAddedDate.setText(it.addedDate)
            } ?: run {
                Toast.makeText(this, "پرونده یافت نشد.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } ?: run {
            Toast.makeText(this, "شناسه پرونده موجود نیست.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveCase() {
        val title = binding.editTitle.text.toString()
        val description = binding.editDescription.text.toString()
        val clientName = binding.editClientName.text.toString()
        val courtDate = binding.editCourtDate.text.toString()
        val status = binding.editStatus.text.toString()
        val addedDate = binding.editAddedDate.text.toString()

        if (title.isBlank() || description.isBlank() || clientName.isBlank() || courtDate.isBlank() || status.isBlank() || addedDate.isBlank()) {
            Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            return
        }

        caseId?.let { id ->
            val currentCases = XmlManager.loadCases(this).toMutableList()
            val index = currentCases.indexOfFirst { it.id == id }
            if (index != -1) {
                currentCases[index] = Case(
                    id = id,
                    title = title,
                    description = description,
                    clientName = clientName,
                    courtDate = courtDate,
                    status = status,
                    addedDate = addedDate
                )
                XmlManager.saveCases(this, currentCases)
                Toast.makeText(this, "پرونده با موفقیت به‌روزرسانی شد.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "پرونده برای به‌روزرسانی یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}