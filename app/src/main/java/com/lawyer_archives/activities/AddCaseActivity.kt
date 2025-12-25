package com.lawyer_archives.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddCaseBinding
import com.lawyer_archives.models.Case
import com.lawyer_archives.utils.DateConverter
import com.lawyer_archives.utils.XmlManager
import com.lawyer_archives.utils.PersianDatePickerHelper

class AddCaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatePicker()
        setupSaveButton()
    }

    private fun setupDatePicker() {
        binding.etCaseDate.setOnClickListener {
            PersianDatePickerHelper.showPersianDatePicker(this) { selectedDate ->
                binding.etCaseDate.setText(selectedDate)
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSaveCase.setOnClickListener {
            val caseTitle = binding.etCaseTitle.text.toString().trim()
            val caseNumber = binding.etCaseNumber.text.toString().trim()
            val caseDescription = binding.etCaseDescription.text.toString().trim()
            val caseDate = binding.etCaseDate.text.toString().trim()
            val clientName = binding.editClientName.text.toString().trim()
            val status = binding.editStatus.text.toString().trim()

            if (caseTitle.isNotEmpty() && caseNumber.isNotEmpty() && caseDescription.isNotEmpty() && caseDate.isNotEmpty()) {
                val case = Case(
                    title = caseTitle,
                    caseNumber = caseNumber,
                    formationDate = caseDate,
                    clientName = clientName,
                    clientRole = "",
                    caseSubject = caseDescription,
                    status = if (status.isEmpty()) "فعال" else status,
                    process = "در جریان",
                    archiveNumber = "",
                    cityJudiciary = "",
                    courtLevelAndType = "",
                    opponentInfo = "",
                    powerOfAttorneyNumber = "",
                    addedDate = DateConverter.getCurrentPersianDate(),
                    courtDate = caseDate
                )

                val existingCases = XmlManager.loadCases(this)
                val updatedCases = existingCases.toMutableList().apply {
                    add(case)
                }

                val success = XmlManager.saveCases(this, updatedCases)
                if (success) {
                    Toast.makeText(this, "پرونده با موفقیت ذخیره شد!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "خطا در ذخیره پرونده.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "لطفاً فیلدهای ضروری را پر کنید.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}