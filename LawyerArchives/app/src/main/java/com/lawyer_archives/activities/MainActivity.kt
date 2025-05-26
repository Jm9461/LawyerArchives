package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.CaseAdapter
import com.lawyer_archives.databinding.ActivityCasesBinding
import com.lawyer_archives.models.Case
import com.lawyer_archives.utils.XmlManager
import com.lawyer_archives.databinding.ActivityMainBinding
import com.lawyer_archives.utils.NotificationUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotificationUtils.createNotificationChannel(this)

        binding.btnViewCases.setOnClickListener {
            startActivity(Intent(this, CasesActivity::class.java))
        }

        binding.btnViewClients.setOnClickListener {
            startActivity(Intent(this, ClientsActivity::class.java))
        }

        binding.btnViewSessions.setOnClickListener {
            startActivity(Intent(this, SessionsActivity::class.java))
        }

        binding.btnViewMeetings.setOnClickListener {
            startActivity(Intent(this, MeetingsActivity::class.java))
        }
    }
}