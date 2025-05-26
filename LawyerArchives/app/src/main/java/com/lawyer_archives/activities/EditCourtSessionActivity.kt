package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditSessionBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.utils.CalendarUtils
import com.lawyer_archives.utils.XmlManager

class EditCourtSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditSessionBinding
    private lateinit var sessionList: MutableList<CourtSession>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val originalSession = intent.getParcelableExtra<CourtSession>("session")!!
        sessionList = XmlManager.loadSessions(this)

        binding.editCaseTitle.setText(originalSession.caseTitle)
        binding.editSessionDate.setText(originalSession.sessionDate)
        binding.editLocation.setText(originalSession.location)
        binding.editDescription.setText(originalSession.description)

        binding.updateSessionButton.setOnClickListener {
            val updatedSession = originalSession.copy(
                caseTitle = binding.editCaseTitle.text.toString(),
                sessionDate = binding.editSessionDate.text.toString(),
                location = binding.editLocation.text.toString(),
                description = binding.editDescription.text.toString()
            )

            val index = sessionList.indexOfFirst { it.id == originalSession.id }
            if (index != -1) {
                sessionList[index] = updatedSession
                XmlManager.saveSessions(this, sessionList)
                CalendarUtils.scheduleSessionAlarm(this, updatedSession)
            }

            finish()
        }
    }
}