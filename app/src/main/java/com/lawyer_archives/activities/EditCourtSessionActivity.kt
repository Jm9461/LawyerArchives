package com.lawyer_archives.activities

import saman.zamani.persiandate.PersianDate
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditSessionBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.utils.XmlManager
import com.lawyer_archives.utils.PersianDatePickerHelper
import java.util.*

class EditCourtSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditSessionBinding
    private var originalSessionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalSessionId = intent.getStringExtra("sessionId")
        setupDatePicker()
        loadSessionData()

        binding.saveSessionButton.setOnClickListener {
            updateSession()
        }
    }

    private fun setupDatePicker() {
        binding.editSessionDate.setOnClickListener { showPersianDatePicker() }
    }

    private fun showPersianDatePicker() {
        val currentDate = binding.editSessionDate.text.toString()
        PersianDatePickerHelper.showPersianDatePicker(this, currentDate) { selectedDate ->
            binding.editSessionDate.setText(selectedDate)
        }
    }

    private fun loadSessionData() {
        originalSessionId?.let { id ->
            val sessionToEdit = XmlManager.loadSessions(this).find { it.id == id }
            sessionToEdit?.let {
                binding.editCaseTitle.setText(it.title)
                binding.editSessionDate.setText(it.sessionDate)
                binding.editLocation.setText(it.location)
                binding.editDescription.setText(it.description)
                binding.checkboxCompleted.isChecked = it.isCompleted
            } ?: run {
                Toast.makeText(this, "جلسه دادگاه برای ویرایش یافت نشد.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } ?: run {
            Toast.makeText(this, "شناسه جلسه دادگاه در دسترس نیست.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateSession() {
        val caseTitle = binding.editCaseTitle.text.toString()
        val sessionDate = binding.editSessionDate.text.toString()
        val location = binding.editLocation.text.toString()
        val description = binding.editDescription.text.toString()
        val isCompleted = binding.checkboxCompleted.isChecked

        if (caseTitle.isBlank() || sessionDate.isBlank() || location.isBlank()) {
            Toast.makeText(this, "لطفاً فیلدهای اجباری را پر کنید.", Toast.LENGTH_SHORT).show()
            return
        }

        originalSessionId?.let { id ->
            val currentSessions = XmlManager.loadSessions(this).toMutableList()
            val index = currentSessions.indexOfFirst { it.id == id }
            if (index != -1) {
                val updatedSession = CourtSession(
                    id = id,
                    title = caseTitle,
                    description = description,
                    clientName = currentSessions[index].clientName,
                    courtDate = sessionDate,
                    courtTime = currentSessions[index].courtTime,
                    courtBranch = location,
                    status = currentSessions[index].status,
                    addedDate = currentSessions[index].addedDate,
                    caseTitle = caseTitle,
                    sessionDate = sessionDate,
                    location = location,
                    caseId = currentSessions[index].caseId,
                    isCompleted = isCompleted
                )
                currentSessions[index] = updatedSession
                XmlManager.saveSessions(this, currentSessions)
                Toast.makeText(this, "جلسه دادگاه با موفقیت به‌روزرسانی شد.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "جلسه دادگاه برای به‌روزرسانی یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}