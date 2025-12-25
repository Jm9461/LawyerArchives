package com.lawyer_archives.activities

import saman.zamani.persiandate.PersianDate
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.R
import com.lawyer_archives.databinding.ActivityAddMeetingBinding
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.utils.XmlManager
import com.lawyer_archives.utils.PersianDatePickerHelper
import java.util.*

class AddMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMeetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDateAndTimePickers()
        setupSaveButton()
    }

    private fun setupDateAndTimePickers() {
        binding.etMeetingDate.setOnClickListener { showPersianDatePicker() }
        binding.etMeetingTime.setOnClickListener { showTimePicker() }
    }

    private fun showPersianDatePicker() {
        PersianDatePickerHelper.showPersianDatePicker(this) { selectedDate ->
            binding.etMeetingDate.setText(selectedDate)
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                binding.etMeetingTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setupSaveButton() {
        binding.btnSaveMeeting.setOnClickListener {
            saveMeeting()
        }
    }

    private fun saveMeeting() {
        val clientName = binding.etClientName.text.toString()
        val meetingDate = binding.etMeetingDate.text.toString()
        val topic = binding.etTitle.text.toString()
        val duration = binding.etMeetingTime.text.toString()
        val location = binding.etDescription.text.toString()

        val reminderOption = when (binding.rgReminderOptions.checkedRadioButtonId) {
            R.id.rb_1_day_before -> "1_day_before"
            R.id.rb_2_days_before -> "2_days_before"
            R.id.rb_3_days_before -> "3_days_before"
            R.id.rb_no_reminder -> "no_reminder"
            else -> "no_reminder"
        }

        if (clientName.isBlank() || meetingDate.isBlank() || topic.isBlank() || duration.isBlank() || location.isBlank()) {
            Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            return
        }

        val persianDate = PersianDate()
        val currentDateString = String.format(
            Locale.getDefault(),
            "%d/%02d/%02d",
            persianDate.shYear,
            persianDate.shMonth,
            persianDate.shDay
        )

        val newMeeting = Meeting(
            id = UUID.randomUUID().toString(),
            clientName = clientName,
            date = meetingDate,
            title = topic,
            time = duration,
            description = location,
            reminderOption = reminderOption,
            addedDate = currentDateString
        )

        val currentMeetings = XmlManager.loadMeetings(this).toMutableList()
        currentMeetings.add(newMeeting)
        XmlManager.saveMeetings(this, currentMeetings)

        Toast.makeText(this, "ملاقات با موفقیت اضافه شد.", Toast.LENGTH_SHORT).show()
        finish()
    }
}