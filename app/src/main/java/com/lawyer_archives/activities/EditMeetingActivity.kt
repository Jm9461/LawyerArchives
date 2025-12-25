package com.lawyer_archives.activities

import saman.zamani.persiandate.PersianDate
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditMeetingBinding
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.utils.XmlManager
import com.lawyer_archives.utils.PersianDatePickerHelper
import java.util.*

class EditMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMeetingBinding
    private var originalMeetingId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalMeetingId = intent.getStringExtra("meetingId")
        setupDateAndTimePickers()
        loadMeetingData()

        binding.btnSaveMeeting.setOnClickListener {
            updateMeeting()
        }
    }

    private fun setupDateAndTimePickers() {
        binding.editMeetingDate.setOnClickListener { showPersianDatePicker() }
        binding.editDuration.setOnClickListener { showTimePicker() }
    }

    private fun showPersianDatePicker() {
        val currentDate = binding.editMeetingDate.text.toString()
        PersianDatePickerHelper.showPersianDatePicker(this, currentDate) { selectedDate ->
            binding.editMeetingDate.setText(selectedDate)
        }
    }

    private fun showTimePicker() {
        val currentTime = binding.editDuration.text.toString()
        val calendar = Calendar.getInstance()

        if (currentTime.isNotEmpty()) {
            try {
                val parts = currentTime.split(":")
                if (parts.size == 2) {
                    calendar.set(Calendar.HOUR_OF_DAY, parts[0].toInt())
                    calendar.set(Calendar.MINUTE, parts[1].toInt())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        TimePickerDialog(
            this,
            { _, hour, minute ->
                binding.editDuration.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun loadMeetingData() {
        originalMeetingId?.let { id ->
            val meetingToEdit = XmlManager.loadMeetings(this).find { it.id == id }
            meetingToEdit?.let {
                binding.editClientName.setText(it.clientName)
                binding.editMeetingDate.setText(it.date)
                binding.editTopic.setText(it.title)
                binding.editDuration.setText(it.time)
                binding.editLocation.setText(it.description)
            } ?: run {
                Toast.makeText(this, "ملاقات برای ویرایش یافت نشد.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } ?: run {
            Toast.makeText(this, "شناسه ملاقات در دسترس نیست.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateMeeting() {
        val clientName = binding.editClientName.text.toString()
        val meetingDate = binding.editMeetingDate.text.toString()
        val topic = binding.editTopic.text.toString()
        val duration = binding.editDuration.text.toString()
        val location = binding.editLocation.text.toString()

        if (clientName.isBlank() || meetingDate.isBlank() || topic.isBlank() || duration.isBlank() || location.isBlank()) {
            Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            return
        }

        originalMeetingId?.let { id ->
            val currentMeetings = XmlManager.loadMeetings(this).toMutableList()
            val index = currentMeetings.indexOfFirst { it.id == id }
            if (index != -1) {
                val updatedMeeting = Meeting(
                    id = id,
                    clientName = clientName,
                    date = meetingDate,
                    title = topic,
                    time = duration,
                    description = location,
                    reminderOption = currentMeetings[index].reminderOption,
                    addedDate = currentMeetings[index].addedDate
                )
                currentMeetings[index] = updatedMeeting
                XmlManager.saveMeetings(this, currentMeetings)
                Toast.makeText(this, "ملاقات با موفقیت به‌روزرسانی شد.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "ملاقات برای به‌روزرسانی یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}