package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditMeetingBinding
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.utils.XmlManager

class EditMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMeetingBinding
    private lateinit var meetingList: MutableList<Meeting>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val originalMeeting = intent.getParcelableExtra<Meeting>("meeting")!!
        meetingList = XmlManager.loadMeetings(this)

        binding.editClientName.setText(originalMeeting.clientName)
        binding.editMeetingDate.setText(originalMeeting.meetingDate)
        binding.editTopic.setText(originalMeeting.topic)
        binding.editDuration.setText(originalMeeting.duration)
        binding.editLocation.setText(originalMeeting.location)

        binding.updateMeetingButton.setOnClickListener {
            val updatedMeeting = originalMeeting.copy(
                clientName = binding.editClientName.text.toString(),
                meetingDate = binding.editMeetingDate.text.toString(),
                topic = binding.editTopic.text.toString(),
                duration = binding.editDuration.text.toString(),
                location = binding.editLocation.text.toString()
            )

            val index = meetingList.indexOfFirst { it.id == originalMeeting.id }
            if (index != -1) {
                meetingList[index] = updatedMeeting
                XmlManager.saveMeetings(this, meetingList)
            }

            finish()
        }
    }
}