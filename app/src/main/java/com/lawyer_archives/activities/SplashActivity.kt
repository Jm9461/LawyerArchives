package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.R
import com.lawyer_archives.helpers.SessionManager

class SplashActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sessionManager = SessionManager(this)

        // اصلاح: اطمینان از استفاده صحیح از Handler برای Launch Screen
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (sessionManager.isLoggedIn()) {
                Intent(this, DashboardActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000) // 3 seconds
    }
}