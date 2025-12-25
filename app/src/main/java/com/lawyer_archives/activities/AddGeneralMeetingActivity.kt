package com.lawyer_archives.activities

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.R
import com.lawyer_archives.models.GeneralMeeting
import com.lawyer_archives.utils.PersianDatePickerHelper
import com.lawyer_archives.utils.XmlManager
import saman.zamani.persiandate.PersianDate
import java.util.Calendar
import java.util.UUID

class AddGeneralMeetingActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etMeetingDate: EditText
    private lateinit var etMeetingTime: EditText
    private lateinit var etLocation: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSaveMeeting: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_general_meeting)

        // Initialize views
        initViews()

        etMeetingDate.setOnClickListener { showPersianDatePicker() }
        etMeetingTime.setOnClickListener { showTimePicker() }
        btnSaveMeeting.setOnClickListener { saveMeeting() }
    }

    private fun initViews() {
        etTitle = findViewById(R.id.etTitle)
        etMeetingDate = findViewById(R.id.etMeetingDate)
        etMeetingTime = findViewById(R.id.etMeetingTime)
        etLocation = findViewById(R.id.etLocation)
        etDescription = findViewById(R.id.etDescription)
        btnSaveMeeting = findViewById(R.id.btnSaveMeeting)
    }

    private fun showPersianDatePicker() {
        PersianDatePickerHelper.showPersianDatePicker(this) { date ->
            etMeetingDate.setText(date)
        }
    }

    private fun showTimePicker() {
        val c = Calendar.getInstance()
        TimePickerDialog(this, { _, h, m ->
            etMeetingTime.setText("%02d:%02d".format(h, m))
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
    }

    private fun saveMeeting() {
        val title = etTitle.text.toString().trim()
        val date = etMeetingDate.text.toString().trim()
        val time = etMeetingTime.text.toString().trim()
        val location = etLocation.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "عنوان، تاریخ و زمان الزامی است", Toast.LENGTH_SHORT).show()
            return
        }

        val persianDate = PersianDate()
        val addedDate = "%04d/%02d/%02d".format(persianDate.shYear, persianDate.shMonth, persianDate.shDay)

        val meeting = GeneralMeeting(
            id = UUID.randomUUID().toString(),
            title = title,
            date = date,
            time = time,
            location = location,
            description = description,
            addedDate = addedDate
        )

        val list = XmlManager.loadGeneralMeetings(this).toMutableList()
        list.add(meeting)
        XmlManager.saveGeneralMeetings(this, list)

        Toast.makeText(this, "جلسه عمومی با موفقیت اضافه شد", Toast.LENGTH_SHORT).show()
        finish()
    }
}