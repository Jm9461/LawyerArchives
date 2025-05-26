package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddSessionBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.utils.CalendarUtils
import com.lawyer_archives.utils.XmlManager
import java.util.*

class AddCourtSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSessionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveSessionButton.setOnClickListener {
            val caseTitle = binding.editCaseTitle.text.toString()
            val sessionDate = binding.editSessionDate.text.toString()
            val location = binding.editLocation.text.toString()
            val description = binding.editDescription.text.toString()

            val session = CourtSession(
                id = UUID.randomUUID().toString(),
                caseId = "",
                caseTitle = caseTitle,
                sessionDate = sessionDate,
                location = location,
                description = description,
                isCompleted = false
            )

            val currentSessions = XmlManager.loadSessions(this)
            currentSessions.add(session)
            XmlManager.saveSessions(this, currentSessions)

            CalendarUtils.scheduleSessionAlarm(this, session)

            finish()
        }
    }
}