package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.R
import com.lawyer_archives.adapters.SessionAdapter
import com.lawyer_archives.databinding.ActivitySessionsBinding
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.utils.XmlManager

/**
 * اکتیویتی مربوط به نمایش، مدیریت، حذف و ویرایش جلسات دادگاه.
 * این کلاس لیست جلسات را از فایل XML خوانده و در یک RecyclerView نمایش می‌دهد.
 */
class SessionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionsBinding
    private lateinit var adapter: SessionAdapter
    private lateinit var sessionList: MutableList<CourtSession>

    /**
     * متد onCreate که هنگام ایجاد اکتیویتی فراخوانی می‌شود.
     * تنظیمات اولیه رابط کاربری و RecyclerView در اینجا انجام می‌شود.
     *
     * @param savedInstanceState وضعیت ذخیره شده قبلی (در صورت وجود).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionList = mutableListOf()
        setupRecyclerView()
        loadSessions()

        binding.fabAddSession.setOnClickListener {
            startActivity(Intent(this, AddCourtSessionActivity::class.java))
        }
    }

    /**
     * متد onResume که هنگام بازگشت به اکتیویتی فراخوانی می‌شود.
     * لیست جلسات مجدداً بارگذاری می‌شود تا تغییرات جدید اعمال شوند.
     */
    override fun onResume() {
        super.onResume()
        loadSessions()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSessions.layoutManager = LinearLayoutManager(this)

        // آرگومان‌ها در خطوط جداگانه قرار گرفتند تا خطای Line length برطرف شود
        adapter = SessionAdapter(
            sessions = sessionList,
            context = this,
            onEditClick = { session ->
                val intent = Intent(this, EditCourtSessionActivity::class.java)
                intent.putExtra("sessionId", session.id)
                startActivity(intent)
            },
            onDeleteClick = { session ->
                confirmAndDeleteSession(session)
            }
        )
        binding.recyclerViewSessions.adapter = adapter
    }

    private fun loadSessions() {
        sessionList = XmlManager.loadSessions(this).toMutableList()

        adapter.updateList(sessionList.toList())

        if (sessionList.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.no_court_sessions_yet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun confirmAndDeleteSession(session: CourtSession) {
        // شکستن خط طولانی برای رعایت محدودیت 100 کاراکتر
        val message = getString(
            R.string.confirm_delete_session_message,
            session.title,
            session.courtDate
        )

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_session_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteSession(session)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun deleteSession(session: CourtSession) {
        sessionList.remove(session)
        XmlManager.saveSessions(this, sessionList)
        loadSessions()
        Toast.makeText(
            this,
            getString(R.string.session_deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()
    }
}