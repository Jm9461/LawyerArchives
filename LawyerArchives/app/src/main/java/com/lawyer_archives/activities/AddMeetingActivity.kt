package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddMeetingBinding
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.utils.XmlManager
import java.util.*

class AddMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMeetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveMeetingButton.setOnClickListener {
            val clientName = binding.editClientName.text.toString()
            val meetingDate = binding.editMeetingDate.text.toString()
            val topic = binding.editTopic.text.toString()
            val duration = binding.editDuration.text.toString()
            val location = binding.editLocation.text.toString()

            val meeting = Meeting(
                id = UUID.randomUUID().toString(),
                clientId = "",
                clientName = clientName,
                meetingDate = meetingDate,
                topic = topic,
                duration = duration,
                location = location
            )

            val currentMeetings = XmlManager.loadMeetings(this)
            currentMeetings.add(meeting)
            XmlManager.saveMeetings(this, currentMeetings)

            finish()
        }
    }
}