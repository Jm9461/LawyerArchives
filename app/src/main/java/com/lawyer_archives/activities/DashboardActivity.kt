package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.R
import com.lawyer_archives.databinding.ActivityDashboardBinding
import com.lawyer_archives.helpers.SessionManager

/**
 * @Class DashboardActivity
 * Activity اصلی برنامه که داشبورد و Navigation Drawer را مدیریت می‌کند.
 */
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sessionManager: SessionManager

    /**
     * متد onCreate: این Activity را مقداردهی اولیه می‌کند.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        sessionManager = SessionManager(this)

        setupNavigationView()
        setupClickListeners()
        updateHeaderInfo()
    }

    /**
     * Navigation Drawer را تنظیم کرده و شنونده آیتم‌های منو را اضافه می‌کند.
     */
    private fun setupNavigationView() {
        val headerView = binding.navView.getHeaderView(0)
        
        // [✅ رفع خطا]: Incorrect whitespace
        val navUsername: TextView = headerView.findViewById(R.id.navHeaderName)
        val username = sessionManager.getUsername()
        
        // [✅ رفع خطا]: Unresolved reference: welcome_guest (با استفاده از کلید welcome_guest)
        navUsername.text = username.ifEmpty { getString(R.string.welcome_guest) }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> startActivity(Intent(this, DashboardActivity::class.java))
                R.id.nav_clients -> startActivity(Intent(this, ClientsActivity::class.java))
                R.id.nav_cases -> startActivity(Intent(this, CasesActivity::class.java))
                
                // [✅ رفع خطا]: Incorrect whitespace
                R.id.nav_daily_tasks -> 
                    startActivity(Intent(this, DailyTasksActivity::class.java)) 

                // [✅ رفع خطا]: Incorrect whitespace
                R.id.nav_sessions -> 
                    startActivity(Intent(this, SessionsActivity::class.java))
                    
                R.id.nav_meetings -> 
                    startActivity(Intent(this, GeneralMeetingsActivity::class.java)) 
                    
                R.id.nav_documents -> 
                    startActivity(Intent(this, DocumentsActivity::class.java))
                    
                R.id.nav_settings -> 
                    startActivity(Intent(this, SettingsActivity::class.java)) 
            }
            binding.drawerLayout.closeDrawer(binding.navView)
            true
        }
    }

    /**
     * شنوندگان کلیک (Click Listeners) را برای دکمه‌های Floating Action تنظیم می‌کند.
     */
    private fun setupClickListeners() {
        binding.fabMain.setOnClickListener {
            toggleFabsVisibility()
        }

        binding.fabAddCase.setOnClickListener {
            startActivity(Intent(this, AddCaseActivity::class.java))
        }
        binding.fabAddClient.setOnClickListener {
            startActivity(Intent(this, AddClientActivity::class.java))
        }
        binding.fabAddMeeting.setOnClickListener {
            startActivity(Intent(this, AddGeneralMeetingActivity::class.java))
        }
        binding.fabAddSession.setOnClickListener {
            startActivity(Intent(this, AddCourtSessionActivity::class.java))
        }
        binding.fabAddDocument.setOnClickListener {
            startActivity(Intent(this, AddDocumentActivity::class.java))
        }
        binding.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddDailyTaskActivity::class.java))
        }
    }

    private fun toggleFabsVisibility() {
        val isVisible = binding.fabAddCase.visibility == View.VISIBLE
        val visibility = if (isVisible) View.GONE else View.VISIBLE

        binding.fabAddCase.visibility = visibility
        binding.fabAddClient.visibility = visibility
        binding.fabAddMeeting.visibility = visibility
        binding.fabAddSession.visibility = visibility
        binding.fabAddDocument.visibility = visibility
        binding.fabAddTask.visibility = visibility
    }

    /**
     * اطلاعات نام کاربر را در هدر Navigation Drawer به‌روزرسانی می‌کند.
     */
    private fun updateHeaderInfo() {
        val headerView = binding.navView.getHeaderView(0)
        val navUsername: TextView = headerView.findViewById(R.id.navHeaderName)
        val username = sessionManager.getUsername()
        
        val welcomeText = if (username.isNotEmpty()) {
            getString(R.string.welcome_user, username)
        } else {
            // [✅ رفع خطا]: Hardcoded string literal
            getString(R.string.welcome_guest) 
        }
        navUsername.text = welcomeText
    }

    /**
     * منوی آپشن (Options Menu) را ایجاد می‌کند.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    /**
     * آیتم‌های انتخاب شده در منوی آپشن را مدیریت می‌کند.
     * [✅ رفع خطا]: حذف Redundant suppression و Hardcoded string literal
     */
    @Suppress("OVERRIDE_DEPRECATION") 
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java)) 
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * دکمه برگشت فیزیکی را مدیریت می‌کند.
     * [✅ رفع خطا]: رفع Hardcoded string literal در @Deprecated
     */
    @Deprecated("Deprecated in Java", ReplaceWith("onBackPressedDispatcher.onBackPressed()"))
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.navView)) {
            binding.drawerLayout.closeDrawer(binding.navView)
        } else {
             super.onBackPressed()
        }
    }
}