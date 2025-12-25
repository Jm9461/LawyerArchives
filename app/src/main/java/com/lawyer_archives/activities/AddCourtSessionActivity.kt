package com.lawyer_archives.activities

import saman.zamani.persiandate.PersianDate
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddCourtSessionBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.receivers.SessionReminderReceiver
import com.lawyer_archives.utils.XmlManager
import com.lawyer_archives.utils.PersianDatePickerHelper
import java.util.*

class AddCourtSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCourtSessionBinding
    private var selectedHour = 0
    private var selectedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourtSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDateAndTimePickers()
        setupSaveButton()
    }

    private fun setupDateAndTimePickers() {
        binding.etCourtDate.setOnClickListener { showPersianDatePicker() }
        binding.etCourtTime.setOnClickListener { showPersianTimePicker() }
    }

    private fun showPersianDatePicker() {
        PersianDatePickerHelper.showPersianDatePicker(this) { selectedDate ->
            binding.etCourtDate.setText(selectedDate)
        }
    }

    private fun showPersianTimePicker() {
        val now = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                binding.etCourtTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
            },
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setupSaveButton() {
        binding.btnSaveSession.setOnClickListener {
            if (validateInputs()) {
                saveSession()
            }
        }
    }

    private fun validateInputs(): Boolean {
        return when {
            binding.etTitle.text.isNullOrEmpty() -> {
                showError("لطفاً عنوان جلسه را وارد کنید")
                false
            }
            binding.etClientName.text.isNullOrEmpty() -> {
                showError("لطفاً نام موکل را وارد کنید")
                false
            }
            binding.etCourtDate.text.isNullOrEmpty() -> {
                showError("لطفاً تاریخ جلسه را انتخاب کنید")
                false
            }
            binding.etCourtTime.text.isNullOrEmpty() -> {
                showError("لطفاً ساعت جلسه را انتخاب کنید")
                false
            }
            else -> true
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveSession() {
        val currentPersianDate = PersianDate()
        val currentDateString = String.format(
            Locale.getDefault(),
            "%d/%02d/%02d",
            currentPersianDate.shYear,
            currentPersianDate.shMonth,
            currentPersianDate.shDay
        )

        val session = CourtSession(
            id = UUID.randomUUID().toString(),
            title = binding.etTitle.text.toString(),
            description = binding.etDescription.text.toString(),
            clientName = binding.etClientName.text.toString(),
            courtDate = binding.etCourtDate.text.toString(),
            courtTime = binding.etCourtTime.text.toString(),
            courtBranch = binding.etCourtBranch.text.toString(),
            status = binding.etStatus.text.toString(),
            addedDate = currentDateString,
            caseTitle = binding.etTitle.text.toString(),
            sessionDate = binding.etCourtDate.text.toString(),
            location = binding.etCourtBranch.text.toString()
        )

        val existingSessions = XmlManager.loadSessions(this)
        val updatedSessions = existingSessions.toMutableList().apply {
            add(session)
        }

        val success = XmlManager.saveSessions(this, updatedSessions)
        if (success) {
            setReminder(session)
            Toast.makeText(this, "جلسه دادرسی با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "خطا در ذخیره جلسه", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setReminder(session: CourtSession) {
        val reminderOption = binding.rgReminderOptions.checkedRadioButtonId

        if (reminderOption == -1 || reminderOption == binding.rbNoReminder.id) return

        try {
            val dateParts = session.courtDate.split("/")
            val timeParts = session.courtTime.split(":")

            if (dateParts.size == 3 && timeParts.size == 2) {
                val persianDate = PersianDate()
                persianDate.initJalaliDate(
                    dateParts[0].toInt(),
                    dateParts[1].toInt(),
                    dateParts[2].toInt()
                )

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = persianDate.toDate().time
                    set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
                    set(Calendar.MINUTE, timeParts[1].toInt())
                    set(Calendar.SECOND, 0)

                    when (reminderOption) {
                        binding.rb1DayBefore.id -> add(Calendar.DAY_OF_YEAR, -1)
                        binding.rb2DaysBefore.id -> add(Calendar.DAY_OF_YEAR, -2)
                        binding.rb3DaysBefore.id -> add(Calendar.DAY_OF_YEAR, -3)
                        binding.rb1WeekBefore.id -> add(Calendar.DAY_OF_YEAR, -7)
                    }
                }

                if (calendar.timeInMillis > System.currentTimeMillis()) {
                    createReminder(calendar.timeInMillis, session)
                } else {
                    Toast.makeText(this, "زمان یادآوری در گذشته است", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "خطا در تبدیل تاریخ شمسی", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createReminder(timeInMillis: Long, session: CourtSession) {
        val intent = Intent(this, SessionReminderReceiver::class.java).apply {
            putExtra("session_id", session.id)
            putExtra("title", session.title)
            putExtra("description", session.description)
            putExtra("client_name", session.clientName)
            putExtra("court_date", session.courtDate)
            putExtra("court_time", session.courtTime)
            putExtra("court_branch", session.courtBranch)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            session.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )

        Toast.makeText(this, "یادآوری تنظیم شد", Toast.LENGTH_SHORT).show()
    }
}