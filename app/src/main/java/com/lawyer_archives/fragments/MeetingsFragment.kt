package com.lawyer_archives.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.MeetingAdapter
import com.lawyer_archives.databinding.FragmentMeetingsBinding
import com.lawyer_archives.helpers.DatabaseHelper
import com.lawyer_archives.helpers.SessionManager
import com.lawyer_archives.models.Meeting

class MeetingsFragment : Fragment() {

    private var _binding: FragmentMeetingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: MeetingAdapter
    private val meetingsList = mutableListOf<Meeting>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())
        sessionManager = SessionManager(requireContext())

        adapter = MeetingAdapter(
            onEditClick = { meeting ->
                Toast.makeText(requireContext(), "ویرایش: ${meeting.title}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { meeting ->
                val deleted = databaseHelper.deleteMeeting(meeting.id)
                if (deleted) {
                    meetingsList.remove(meeting)
                    adapter.updateList(meetingsList)
                    Toast.makeText(requireContext(), "جلسه حذف شد", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "خطا در حذف جلسه", Toast.LENGTH_SHORT).show()
                }
            }
        )

        setupRecyclerView()
        loadMeetings()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MeetingsFragment.adapter
        }
    }

    private fun loadMeetings() {
        val userId = sessionManager.getUserId() ?: return
        val meetings = databaseHelper.getMeetingsByUserId(userId)
        meetingsList.clear()
        meetingsList.addAll(meetings)
        adapter.updateList(meetingsList)

        if (meetingsList.isEmpty()) {
            Toast.makeText(requireContext(), "هیچ جلسه‌ای ثبت نشده است.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadMeetings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}