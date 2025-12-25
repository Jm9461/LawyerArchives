// MeetingsActivity.kt — کاملاً اصلاح‌شده و بدون خطا
package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.MeetingAdapter
import com.lawyer_archives.databinding.ActivityMeetingsBinding
import com.lawyer_archives.models.Meeting
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast

class MeetingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeetingsBinding
    private lateinit var adapter: MeetingAdapter
    private val meetingList = mutableListOf<Meeting>()

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
        // نسخه جدید آداپتور — فقط دو لامبدا
        adapter = MeetingAdapter(
            onEditClick = { meeting ->
                val intent = Intent(this, EditMeetingActivity::class.java).apply {
                    putExtra("meetingId", meeting.id)
                }
                startActivity(intent)
            },
            onDeleteClick = { meeting ->
                confirmAndDeleteMeeting(meeting)
            }
        )

        binding.recyclerViewMeetings.apply {
            layoutManager = LinearLayoutManager(this@MeetingsActivity)
            adapter = this@MeetingsActivity.adapter
        }
    }

    private fun loadMeetings() {
        val loadedMeetings = XmlManager.loadMeetings(this)
        meetingList.clear()
        meetingList.addAll(loadedMeetings)

        adapter.updateList(meetingList)  // حالا updateList وجود داره

        if (meetingList.isEmpty()) {
            Toast.makeText(this, "هنوز ملاقاتی ثبت نشده است.", Toast.LENGTH_LONG).show()
        }
    }

    private fun confirmAndDeleteMeeting(meeting: Meeting) {
        AlertDialog.Builder(this)
            .setTitle("حذف ملاقات")
            .setMessage("آیا از حذف ملاقات با موکل \"${meeting.clientName}\" در تاریخ \"${meeting.date}\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteMeeting(meeting)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    private fun deleteMeeting(meeting: Meeting) {
        meetingList.remove(meeting)
        XmlManager.saveMeetings(this, meetingList)
        adapter.updateList(meetingList)
        Toast.makeText(this, "ملاقات با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadMeetings()
    }
}