package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.MeetingAdapter
import com.lawyer_archives.databinding.ActivityMeetingsBinding
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.utils.XmlManager

class MeetingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeetingsBinding
    private lateinit var adapter: MeetingAdapter
    private lateinit var meetingList: MutableList<Meeting>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadMeetings()

        binding.fabAddMeeting.setOnClickListener {
            startActivity(Intent(this, AddMeetingActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MeetingAdapter(
            onEditClick = { meeting ->
                val intent = Intent(this, EditMeetingActivity::class.java)
                intent.putExtra("meeting", meeting)
                startActivity(intent)
            },
            onDeleteClick = { meeting ->
                deleteMeeting(meeting)
            }
        )
        binding.recyclerView.adapter = adapter
    }

    private fun loadMeetings() {
        meetingList = XmlManager.loadMeetings(this)
        adapter.submitList(meetingList)
    }

    private fun deleteMeeting(meeting: Meeting) {
        meetingList.remove(meeting)
        XmlManager.saveMeetings(this, meetingList)
        adapter.submitList(meetingList.toList())
    }

    override fun onResume() {
        super.onResume()
        loadMeetings()
    }
}