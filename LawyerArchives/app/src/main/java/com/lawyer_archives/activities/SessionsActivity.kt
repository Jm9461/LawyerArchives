package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.SessionAdapter
import com.lawyer_archives.databinding.ActivitySessionsBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.utils.XmlManager

class SessionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionsBinding
    private lateinit var adapter: SessionAdapter
    private lateinit var sessionList: MutableList<CourtSession>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadSessions()

        binding.fabAddSession.setOnClickListener {
            startActivity(Intent(this, AddCourtSessionActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SessionAdapter(
            onEditClick = { session ->
                val intent = Intent(this, EditCourtSessionActivity::class.java)
                intent.putExtra("session", session)
                startActivity(intent)
            },
            onDeleteClick = { session ->
                deleteSession(session)
            }
        )
        binding.recyclerView.adapter = adapter
    }

    private fun loadSessions() {
        sessionList = XmlManager.loadSessions(this)
        adapter.submitList(sessionList)
    }

    private fun deleteSession(session: CourtSession) {
        sessionList.remove(session)
        XmlManager.saveSessions(this, sessionList)
        adapter.submitList(sessionList.toList())
    }

    override fun onResume() {
        super.onResume()
        loadSessions()
    }
}