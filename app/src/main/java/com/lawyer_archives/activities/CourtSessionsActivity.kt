package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.CourtSessionAdapter
import com.lawyer_archives.databinding.ActivityCourtSessionsBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.utils.XmlManager

class CourtSessionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourtSessionsBinding
    private lateinit var adapter: CourtSessionAdapter
    private val courtSessionsList = mutableListOf<CourtSession>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourtSessionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        loadCourtSessions()
    }

    private fun setupRecyclerView() {
        // درست شده: فقط یک پارامتر می‌خواد → onItemClick
        adapter = CourtSessionAdapter { session ->
            val intent = Intent(this@CourtSessionsActivity, EditCourtSessionActivity::class.java).apply {
                putExtra("sessionId", session.id)
            }
            startActivity(intent)
        }

        binding.courtSessionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CourtSessionsActivity)
            adapter = this@CourtSessionsActivity.adapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAddCourtSession.setOnClickListener {
            startActivity(Intent(this, AddCourtSessionActivity::class.java))
        }

        binding.toolbarCourtSessions.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadCourtSessions() {
        val sessions = XmlManager.loadSessions(this) // یا loadCourtSessions(this)

        courtSessionsList.clear()
        courtSessionsList.addAll(sessions)

        // اگر از ListAdapter استفاده می‌کردی submitList می‌زدی، ولی الان از Adapter معمولی استفاده می‌کنی
        adapter.updateList(courtSessionsList)

        // مدیریت حالت خالی بودن
        if (courtSessionsList.isEmpty()) {
            binding.emptyStateLayout.visibility = android.view.View.VISIBLE
            binding.courtSessionsRecyclerView.visibility = android.view.View.GONE
        } else {
            binding.emptyStateLayout.visibility = android.view.View.GONE
            binding.courtSessionsRecyclerView.visibility = android.view.View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        loadCourtSessions() // رفرش بعد از اضافه/ویرایش
    }
}